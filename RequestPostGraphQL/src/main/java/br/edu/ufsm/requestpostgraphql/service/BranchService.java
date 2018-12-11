package br.edu.ufsm.requestpostgraphql.service;

import br.edu.ufsm.requestpostgraphql.entity.Branch;
import br.edu.ufsm.requestpostgraphql.entity.PullRequest;
import br.edu.ufsm.requestpostgraphql.entity.Repository;
import br.edu.ufsm.requestpostgraphql.repository.BranchRepository;
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
public class BranchService {
    BranchRepository repository = new BranchRepository();
    RequestService requestService = new RequestService();
    public void extractBranch(Repository r) {
        JSONObject jsonResponse = requestService.getHttpClient(getQuery(r));
        if (jsonResponse != null) {
            List<Branch> branchs = this.getBranch(jsonResponse, r);
            repository.save(branchs);
        }
    }

    public String getQuery(Repository r) {
        StringBuilder sb = new StringBuilder();
        sb.append("query {");
        sb.append("  repository(owner: \\\"").append(r.getOwner()).append("\\\", name: \\\"").append(r.getName()).append("\\\") {");
        sb.append("    refs(first: 100, refPrefix: \\\"refs/heads/\\\") {");
        sb.append("      totalCount");
        sb.append("      edges {");
        sb.append("        node {");
        sb.append("     id     name");
        sb.append("        }");
        sb.append("      }");
        sb.append("      pageInfo {");
        sb.append("        endCursor");
        sb.append("        hasNextPage");
        sb.append("      }");
        sb.append("    }");
        sb.append("  }");
        sb.append("rateLimit {     limit     cost     remaining     resetAt   }");
        sb.append("}");
        return "{\"query\": \"" + sb.toString() + "\"}";
    }

    public List<Branch> getBranch(JSONObject jsonResponse, Repository r) {
        List<Branch> lista = new ArrayList<>();
        try {
            JSONArray issueJson = jsonResponse.getJSONObject("data").getJSONObject("repository").getJSONObject("refs").getJSONArray("edges");
            for (int i = 0; i < issueJson.length(); i++) {
                Branch branch = new Branch();
                JSONObject objectJson;

                objectJson = issueJson.getJSONObject(i).getJSONObject("node");
                branch.setName(objectJson.getString("name"));
                branch.setId(objectJson.getString("id"));
                branch.setRepository(r.getName());
                branch.setOwner(r.getOwner());
                lista.add(branch);
            }
        } catch (JSONException ex) {
            Logger.getLogger(PullRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}
