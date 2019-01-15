package br.edu.ufsm.requestpostgraphql.service;

import br.edu.ufsm.requestpostgraphql.entity.Complex;
import br.edu.ufsm.requestpostgraphql.entity.PullComment;
import br.edu.ufsm.requestpostgraphql.entity.PullRequest;
import br.edu.ufsm.requestpostgraphql.entity.Repository;
import br.edu.ufsm.requestpostgraphql.entity.Simple;
import br.edu.ufsm.requestpostgraphql.entity.Status;
import br.edu.ufsm.requestpostgraphql.repository.PullRequestCommentRepository;
import br.edu.ufsm.requestpostgraphql.repository.PullRequestRepository;
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
 * @since 19/12/2018
 */
public class PullRequestService {
    PullRequestRepository repository = new PullRequestRepository();
    PullRequestCommentRepository repositoryComment = new PullRequestCommentRepository();
    PullRequestCommentService serviceComment = new PullRequestCommentService();
    public List<PullRequest> getPull(JSONObject json, Repository r) {
        List<PullRequest> lista = new ArrayList<>();
        try {
            JSONArray pull = json.getJSONObject("data").getJSONObject("repository").getJSONObject("pullRequests").getJSONArray("edges");
//            System.out.println(json.getJSONObject("data").getJSONObject("repository").getJSONObject("pullRequests").getString("totalCount"));
            for (int i = 0; i < pull.length(); i++) {
                PullRequest pullObject = new PullRequest();
                JSONObject p;

                p = pull.getJSONObject(i).getJSONObject("pullRequest");
                pullObject.setId(p.getString("id"));
                pullObject.setNumber(p.getString("number"));
                pullObject.setTitle(p.getString("title"));
                pullObject.setCreatedAt(p.getString("createdAt"));
                pullObject.setUpdatedAt(p.getString("updatedAt"));
                pullObject.setUrl(p.getString("url"));
                pullObject.setBodyHTML(p.getString("bodyHTML"));
                if (!p.isNull("author")) {
                    pullObject.setAuthor(p.getJSONObject("author").getString("login"));
                }

                pullObject.setOwner(r.getOwner());
                pullObject.setName(r.getName());
                lista.add(pullObject);

                if (!p.isNull("comments")) {
                    JSONArray objectComments = p.getJSONObject("comments").getJSONArray("edges");
                    pullObject.setComments(p.getJSONObject("comments").getInt("totalCount"));
                    List<PullComment> comments = this.serviceComment.getComment(objectComments, r, pullObject);
                    this.repositoryComment.save(comments);
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(PullRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public Complex getQuery(String cursor) {
        Complex c = new Complex("pullRequests", "pullRequest",
                new String[]{"id", "number", "title", "createdAt", "updatedAt", "url", "bodyHTML"},
                new Simple[]{new Simple("author", new String[]{"login"})});
        c.setComplex(new Complex[]{this.serviceComment.getQuery()});
        c.setEndCusor(cursor);
        return c;
    }
    
    public Status getStatus(JSONObject jsonResponse, Status status) throws JSONException {
        boolean hasNextPage = jsonResponse.getJSONObject("data").getJSONObject("repository").getJSONObject("pullRequests").getJSONObject("pageInfo").getBoolean("hasNextPage");
        String endCursor = jsonResponse.getJSONObject("data").getJSONObject("repository").getJSONObject("pullRequests").getJSONObject("pageInfo").getString("endCursor");
        status.setCompletePull(!hasNextPage);
        status.setEndCursorPull(endCursor);
        return status;
    }

    public Status extract(JSONObject jsonResponse, Repository r, Status status) throws JSONException {
        status = this.getStatus(jsonResponse, status);
        List<PullRequest> pulls = this.getPull(jsonResponse, r);
        this.repository.save(pulls);
        return status;
    }
    
}
