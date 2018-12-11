/*
 */
package br.edu.ufsm.requestpostgraphql.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Persistence;
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
public class PullRequest implements Serializable {

    @Id
    private String id;
    private String number;
    @Column(length = 1000)
    @Lob
    private String title;
    private String createdAt;
    private String updatedAt;
    private String author;
    private String owner;
    private String name;
    private String url;
    private int comments;
    public static List<PullRequest> getPull(JSONObject json, Repository r) {
        List<PullRequest> lista = new ArrayList<>();
        try {
            JSONArray pull = json.getJSONObject("data").getJSONObject("repository").getJSONObject("pullRequests").getJSONArray("edges");
//            System.out.println(json.getJSONObject("data").getJSONObject("repository").getJSONObject("pullRequests").getString("totalCount"));
            for (int i = 0; i < pull.length(); i++) {
                PullRequest pullObject = new PullRequest();
                JSONObject p;

                p = pull.getJSONObject(i).getJSONObject("pullRequest");
                pullObject.id = p.getString("id");
                pullObject.number = p.getString("number");
                pullObject.title = p.getString("title");
                pullObject.createdAt = p.getString("createdAt");
                pullObject.updatedAt = p.getString("updatedAt");
                pullObject.url = p.getString("url");
                if (!p.isNull("author")) {
                    pullObject.author = p.getJSONObject("author").getString("login");
                }

                pullObject.owner = r.getOwner();
                pullObject.name = r.getName();
                lista.add(pullObject);

                if (!p.isNull("comments")) {
                    JSONArray objectComments = p.getJSONObject("comments").getJSONArray("edges");
                    pullObject.comments = p.getJSONObject("comments").getInt("totalCount");
                    PullComment.save(objectComments, r, pullObject);
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(PullRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public static Complex getQuery() {
        Complex c = new Complex("pullRequests", "pullRequest",
                new String[]{"id", "number", "title", "createdAt", "updatedAt", "url"},
                new Simple[]{new Simple("author", new String[]{"login"})});
        c.setComplex(new Complex[]{PullComment.getQuery()});
        return c;
    }

    @Override
    public String toString() {
        return "Id: " + this.id + " number:" + this.number;
    }

    public static boolean save(JSONObject json, Repository r) {
        List<PullRequest> lista = PullRequest.getPull(json, r);
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
