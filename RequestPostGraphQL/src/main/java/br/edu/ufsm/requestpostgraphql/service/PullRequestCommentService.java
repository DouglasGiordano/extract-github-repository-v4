package br.edu.ufsm.requestpostgraphql.service;

import br.edu.ufsm.requestpostgraphql.entity.Complex;
import br.edu.ufsm.requestpostgraphql.entity.PullComment;
import br.edu.ufsm.requestpostgraphql.entity.PullRequest;
import br.edu.ufsm.requestpostgraphql.entity.Repository;
import br.edu.ufsm.requestpostgraphql.entity.Simple;
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
public class PullRequestCommentService {

    public List<PullComment> getComment(JSONArray json, Repository r, PullRequest pull) {
        List<PullComment> lista = new ArrayList<>();
        try {
            for (int i = 0; i < json.length(); i++) {
                PullComment comment = new PullComment();
                JSONObject objectJson;

                objectJson = json.getJSONObject(i).getJSONObject("comment");
                comment.setId(objectJson.getString("id"));
                comment.setBodyHTML(objectJson.getString("bodyHTML"));
                comment.setCreatedAt(objectJson.getString("createdAt"));
                comment.setUpdatedAt(objectJson.getString("updatedAt"));
                comment.setUrl(objectJson.getString("url"));
                if (!objectJson.isNull("author")) {
                    comment.setAuthor(objectJson.getJSONObject("author").getString("login"));
                }
                comment.setOwner(r.getOwner());
                comment.setName(r.getName());
                comment.setPull(pull.getId());
                lista.add(comment);
            }
        } catch (JSONException ex) {
            Logger.getLogger(PullRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public Complex getQuery() {
        return new Complex("comments", "comment",
                new String[]{"id", "url", "bodyHTML", "createdAt", "updatedAt"},
                new Simple[]{new Simple("author", new String[]{"login"})});
    }
}
