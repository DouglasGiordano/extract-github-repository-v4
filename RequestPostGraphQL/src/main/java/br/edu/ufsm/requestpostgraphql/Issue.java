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
public class Issue implements Serializable {

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

    public static List<Issue> getIssue(JSONObject json, Repository r) {
        List<Issue> lista = new ArrayList<>();
        try {
            JSONArray issueJson = json.getJSONObject("data").getJSONObject("repository").getJSONObject("issues").getJSONArray("edges");
//            System.out.println(json.getJSONObject("data").getJSONObject("repository").getJSONObject("issues").getString("totalCount"));
//            System.out.println(json.getJSONObject("data").getJSONObject("repository").getJSONObject("issues").getString("pageInfo"));
            for (int i = 0; i < issueJson.length(); i++) {
                Issue issue = new Issue();
                JSONObject objectJson;

                objectJson = issueJson.getJSONObject(i).getJSONObject("issue");
                issue.id = objectJson.getString("id");
                issue.number = objectJson.getString("number");
                issue.title = objectJson.getString("title");
                issue.createdAt = objectJson.getString("createdAt");
                issue.updatedAt = objectJson.getString("updatedAt");
                issue.url = objectJson.getString("url");
                if (!objectJson.isNull("author")) {
                    issue.author = objectJson.getJSONObject("author").getString("login");
                }
                issue.owner = r.getOwner();
                issue.name = r.getName();
                lista.add(issue);

                if (!objectJson.isNull("comments")) {
                    JSONArray objectComments = objectJson.getJSONObject("comments").getJSONArray("edges");
                    issue.comments = objectJson.getJSONObject("comments").getInt("totalCount");
                    IssueComment.save(objectComments, r, issue);
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(PullRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public static Complex getQuery() {
        Complex c = new Complex("issues", "issue",
                new String[]{"id", "number", "title", "createdAt", "updatedAt", "url"},
                new Simple[]{new Simple("author", new String[]{"login"})});
        c.setComplex(new Complex[]{IssueComment.getQuery()});
        return c;
    }

    @Override
    public String toString() {
        return "Id: " + this.id + " number:" + this.number;
    }

    public static boolean save(JSONObject json, Repository r) {
        List<Issue> lista = Issue.getIssue(json, r);
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
