package br.edu.ufsm.requestpostgraphql.service;

import br.edu.ufsm.requestpostgraphql.entity.Complex;
import br.edu.ufsm.requestpostgraphql.entity.Issue;
import br.edu.ufsm.requestpostgraphql.entity.IssueComment;
import br.edu.ufsm.requestpostgraphql.entity.PullRequest;
import br.edu.ufsm.requestpostgraphql.entity.Repository;
import br.edu.ufsm.requestpostgraphql.entity.Simple;
import br.edu.ufsm.requestpostgraphql.entity.Status;
import br.edu.ufsm.requestpostgraphql.repository.IssueCommentRepository;
import br.edu.ufsm.requestpostgraphql.repository.IssueRepository;
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
 * @since 18/12/2018
 */
public class IssueService {

    private IssueRepository repository = new IssueRepository();
    private IssueCommentService commentService = new IssueCommentService();
    private IssueCommentRepository commentRepository = new IssueCommentRepository();
    public List<Issue> getIssue(JSONObject json, Repository r) {
        List<Issue> lista = new ArrayList<>();
        try {
            JSONArray issueJson = json.getJSONObject("data").getJSONObject("repository").getJSONObject("issues").getJSONArray("edges");
//            System.out.println(json.getJSONObject("data").getJSONObject("repository").getJSONObject("issues").getString("totalCount"));
//            System.out.println(json.getJSONObject("data").getJSONObject("repository").getJSONObject("issues").getString("pageInfo"));
            for (int i = 0; i < issueJson.length(); i++) {
                Issue issue = new Issue();
                JSONObject objectJson;

                objectJson = issueJson.getJSONObject(i).getJSONObject("issue");
                issue.setId(objectJson.getString("id"));
                issue.setNumber(objectJson.getString("number"));
                issue.setTitle(objectJson.getString("title"));
                issue.setCreatedAt(objectJson.getString("createdAt"));
                issue.setUpdatedAt(objectJson.getString("updatedAt"));
                issue.setUrl(objectJson.getString("url"));
                issue.setBodyHTML(objectJson.getString("bodyHTML"));
                if (!objectJson.isNull("author")) {
                    issue.setAuthor(objectJson.getJSONObject("author").getString("login"));
                }
                issue.setOwner(r.getOwner());
                issue.setName(r.getName());
                lista.add(issue);

                if (!objectJson.isNull("comments")) {
                    JSONArray objectComments = objectJson.getJSONObject("comments").getJSONArray("edges");
                    issue.setComments(objectJson.getJSONObject("comments").getInt("totalCount"));
                    List<IssueComment> comments = this.commentService.getComment(objectComments, r, issue);
                    this.commentRepository.save(comments);
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(PullRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public Complex getQuery(String cursor) {
        Complex c = new Complex("issues", "issue",
                new String[]{"id", "number", "title", "createdAt", "updatedAt", "url", "bodyHTML"},
                new Simple[]{new Simple("author", new String[]{"login"})});
        c.setEndCusor(cursor);
        c.setComplex(new Complex[]{this.commentService.getQuery()});
        return c;
    }

    public Status getStatus(JSONObject jsonResponse, Status status) throws JSONException {
        boolean hasNextPage = jsonResponse.getJSONObject("data").getJSONObject("repository").getJSONObject("issues").getJSONObject("pageInfo").getBoolean("hasNextPage");
        String endCursor = jsonResponse.getJSONObject("data").getJSONObject("repository").getJSONObject("issues").getJSONObject("pageInfo").getString("endCursor");
        status.setCompleteIssue(!hasNextPage);
        status.setEndCursorIssue(endCursor);
        return status;
    }

    public Status extract(JSONObject jsonResponse, Repository r, Status status) throws JSONException {
        status = this.getStatus(jsonResponse, status);
        List<Issue> issues = this.getIssue(jsonResponse, r);
        this.repository.save(issues);
        return status;
    }

}
