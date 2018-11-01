/*
 */
package br.edu.ufsm.requestpostgraphql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Douglas Giordano
 */
@Data
@Entity
public class IssueComment implements Serializable {

    @Id
    private String id;
    @Column(length = 10000)
    @Lob
    private String bodyHTML;
    private String createdAt;
    private String updatedAt;
    private String author;
    private String owner;
    private String url;
    private String name;
    private String issue;

    public static List<IssueComment> getComment(JSONArray json, Repository r, Issue issue) {
        List<IssueComment> lista = new ArrayList<>();
        try {
            for (int i = 0; i < json.length(); i++) {
                IssueComment comment = new IssueComment();
                JSONObject objectJson;

                objectJson = json.getJSONObject(i).getJSONObject("comment");
                comment.id = objectJson.getString("id");
                comment.bodyHTML = objectJson.getString("bodyHTML");
                comment.createdAt = objectJson.getString("createdAt");
                comment.updatedAt = objectJson.getString("updatedAt");
                comment.url = objectJson.getString("url");
                if (!objectJson.isNull("author")) {
                    comment.author = objectJson.getJSONObject("author").getString("login");
                }
                comment.owner = r.getOwner();
                comment.name = r.getName();
                comment.issue = issue.getId();
                lista.add(comment);
            }
        } catch (JSONException ex) {
            Logger.getLogger(PullRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public static Complex getQuery() {
        return new Complex("comments", "comment",
                new String[]{"id", "url", "bodyHTML", "createdAt", "updatedAt"},
                new Simple[]{new Simple("author", new String[]{"login"})});
    }

    @Override
    public String toString() {
        return "Id: " + this.id + " url:" + this.url;
    }

    public static boolean save(JSONArray json, Repository r, Issue issue) {
        List<IssueComment> lista = IssueComment.getComment(json, r, issue);
        EntityManager manager = PersistenceGithub.openEntityManager();
        manager.getTransaction().begin();
        lista.forEach((p) -> {
            manager.merge(p);
        });
        manager.getTransaction().commit();
        manager.clear();
        return true;
    }
}
