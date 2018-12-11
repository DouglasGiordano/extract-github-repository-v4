package br.edu.ufsm.requestpostgraphql.service;

import br.edu.ufsm.requestpostgraphql.entity.Branch;
import br.edu.ufsm.requestpostgraphql.entity.BranchCommit;
import br.edu.ufsm.requestpostgraphql.entity.Commit;
import br.edu.ufsm.requestpostgraphql.entity.Repository;
import br.edu.ufsm.requestpostgraphql.repository.BranchRepository;
import br.edu.ufsm.requestpostgraphql.repository.CommitRepository;
import static java.lang.String.format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Douglas Giordano
 */
public class CommitService {

    private CommitRepository commitRepository = new CommitRepository();
    private RequestService requestService = new RequestService();
    private BranchService branchService = new BranchService();
    private BranchRepository branchRepository = new BranchRepository();

    public String getQuery(Repository r, List<Branch> branchs) {
        StringBuilder sb = new StringBuilder();
        sb.append("query { ");
        sb.append("repository(owner: \\\"").append(r.getOwner()).append("\\\", name: \\\"").append(r.getName()).append("\\\") {");
        int n = 0;
        for (Branch branch : branchs) {
            String cursor = "";
            if (branch.getEndCursorCommit() != null) {
                cursor = ", after: \\\"" + branch.getEndCursorCommit() + "\\\"";
            }
            if (!branch.isComplete()) {
                n++;
                sb.append("branch").append(n).append(": ref(qualifiedName: \\\"").append(branch.getName()).append("\\\") {name ");
                sb.append("target {");
                sb.append("... on Commit {");
                sb.append("     history(first: 100").append(cursor).append(") { ");
                sb.append("totalCount ");
                sb.append("nodes { ");
                sb.append("id ");
                sb.append("additions ");
                sb.append("deletions ");
                sb.append("changedFiles ");
                sb.append("committer{name user{login}} ");
                sb.append("authoredByCommitter ");
                sb.append("authoredDate ");
                sb.append("committedDate ");
                sb.append("url ");
                sb.append("message ");
                sb.append("author {name user{login}}");
                sb.append("} ");
                sb.append("pageInfo { ");
                sb.append("hasNextPage ");
                sb.append("endCursor ");
                sb.append("} ");
                sb.append("   } ");
                sb.append("  } ");
                sb.append(" } ");
                sb.append("} ");
            }
            if (n == 2) {
                break;
            }
        }
        sb.append("  } ");
        sb.append("rateLimit {     limit     cost     remaining     resetAt   }");
        sb.append("}");
        return "{\"query\" : \"" + sb.toString() + "\"}";
    }

    public void extractCommit(Repository r) {
        List<Branch> branchs = branchRepository.getBranchIncomplete(r);
        while (branchs.size() > 0) {
            JSONObject jsonResponse = requestService.getHttpClient(getQuery(r, branchs));
            if (jsonResponse != null) {
                List<Commit> commits = getCommit(r, jsonResponse);
                commitRepository.save(commits);
            }
            branchs = branchRepository.getBranchIncomplete(r);
        }
    }

    public List<Commit> getCommit(Repository r, JSONObject jsonResponse) {
        List<Commit> lista = new ArrayList<>();

        try {
            final JSONObject branchCommitJson = jsonResponse.getJSONObject("data").getJSONObject("repository");
            branchCommitJson.keys().forEachRemaining(indexBranch -> {
                getJsonCommit(branchCommitJson, (String) indexBranch, r, lista);
            });

        } catch (JSONException ex) {
            Logger.getLogger(CommitService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
    }

    public void getJsonCommit(JSONObject branchCommitJson, String indexBranch, Repository r, List lista) {
        try {
            System.out.println(indexBranch);
            if (branchCommitJson.isNull((String) indexBranch)) {
                System.out.println("Branch is null " + "(" + r.getOwner() + "/" + r.getName() + ")" + ".");
                return;
            }
            System.out.println("(" + r.getOwner() + "/" + r.getName() + ") starting...");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            JSONObject branchJson;
            branchJson = branchCommitJson.getJSONObject((String) indexBranch);
            String nameBranch = branchJson.getString("name");
            JSONObject objectJson = branchJson.getJSONObject("target").getJSONObject("history");

            JSONArray jsoncommits = objectJson.getJSONArray("nodes");
            JSONObject pageInfo = objectJson.getJSONObject("pageInfo");
            boolean hasNextPage = pageInfo.getBoolean("hasNextPage");
            String endCursor = pageInfo.getString("endCursor");
            Branch branch = branchRepository.getBranch(nameBranch, r);
            for (int j = 0; j < jsoncommits.length(); j++) {
                JSONObject jsoncommit = (JSONObject) jsoncommits.get(j);
                Commit c = new Commit();
                c.setId(jsoncommit.getString("id"));
                c.setAdditions(jsoncommit.getInt("additions"));
                c.setDeletions(jsoncommit.getInt("deletions"));
                c.setChangedFiles(jsoncommit.getInt("changedFiles"));
                JSONObject commiterJson = jsoncommit.getJSONObject("committer");
                if (!commiterJson.isNull("user")) {
                    String commiter = commiterJson.getJSONObject("user").getString("login");
                    c.setCommitter(commiter);
                } else {
                    c.setCommiterNotFound(true);
                    c.setCommitter(commiterJson.getString("name"));

                }
                c.setAuthoredByCommitter(jsoncommit.getBoolean("authoredByCommitter"));
                try {
                    c.setAuthoredDate(format.parse(jsoncommit.getString("authoredDate")));
                    c.setCommittedDate(format.parse(jsoncommit.getString("committedDate")));
                } catch (ParseException ex) {
                    System.out.println(ex);
                }
                c.setUrl(jsoncommit.getString("url"));
                c.setMessage(jsoncommit.getString("message"));
                JSONObject authorJson = jsoncommit.getJSONObject("author");
                if (!authorJson.isNull("user")) {
                    String author = authorJson.getJSONObject("user").getString("login");
                    c.setAuthor(author);
                } else {
                    c.setAuthorNotFound(true);
                    c.setAuthor(authorJson.getString("name"));
                }
                c.setName(r.getName());
                c.setOwner(r.getOwner());
                lista.add(c);
                BranchCommit bc = new BranchCommit(branch, c);
                branchRepository.save(bc);
            }

            branch.setComplete(!hasNextPage);
            branch.setEndCursorCommit(endCursor);
            branchRepository.save(branch);
            
            System.out.println(branch.getName() + "(" + r.getOwner() + "/" + r.getName() + ") downloading...");

        } catch (JSONException ex) {
            System.out.println("Error in extract info " + "(" + r.getOwner() + "/" + r.getName() + "):" + ex);
        }
    }

}
