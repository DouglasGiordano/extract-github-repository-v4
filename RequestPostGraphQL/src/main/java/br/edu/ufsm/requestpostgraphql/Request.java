/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.requestpostgraphql;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dougl
 */
public class Request {

    private final String url = "https://api.github.com/graphql";
    private int tokenNow = 1;
    private final String[] tokens = {"406a7772a30e9a9e28c7a95b8d4b26cafbe1b339",
        "524396afd5ee7708eece891bd5ae822a23414496", "ea73ffe6e846ea887eaed9a11ab5759d8fb88962", "8e42a97bfca09213c1a3129506c72e4b729ddaec"};
    private String query = "";
    private HasNext hasNextPull = new HasNext(true, "");
    private HasNext hasNextIssue = new HasNext(true, "");
    private Status statusProject;
    private int tentativas = 0;
    public String getQuery(Repository repository) {
        Complex pullRequest = PullRequest.getQuery();
        Complex issue = Issue.getQuery();
        repository.setFields(new Complex[]{pullRequest, issue});
        return "{\"query\": \"" + repository.toString() + "\"}";
    }

    public boolean verifyLimitNow(JSONObject json) throws JSONException {
        int cost = json.getJSONObject("data").getJSONObject("rateLimit").getInt("cost");
        int remaining = json.getJSONObject("data").getJSONObject("rateLimit").getInt("remaining");

        return remaining < cost;
    }

    public String getQueryEndCursor(Repository repository) {
        repository.setFields(new ArrayList<Complex>());
        if (hasNextPull.isHasNext()) {
            Complex pullRequest = PullRequest.getQuery();
            pullRequest.setEndCusor(hasNextPull.getEndCursor());
            repository.getFields().add(pullRequest);
        }
        if (hasNextIssue.isHasNext()) {
            Complex issue = Issue.getQuery();
            issue.setEndCusor(hasNextIssue.getEndCursor());
            repository.getFields().add(issue);
        }
        System.out.println("Request for repository " + repository.getOwner() + " - " + repository.getName());
        return "{\"query\": \"" + repository.toString() + "\"}";
    }

    public void extract(Repository r) {
        JSONObject jsonResponse = this.getHttpClient(this.query);
        if (jsonResponse != null) {
            this.getValues(jsonResponse, r);
            if (hasNext(jsonResponse)) {
                this.query = getQueryEndCursor(r);
                extract(r);
            }
        }

    }

    public void getValues(JSONObject jsonObject, Repository r) {
        if (hasNextIssue.isHasNext()) {
            Issue.save(jsonObject, r);
        }

        if (hasNextPull.isHasNext()) {
            PullRequest.save(jsonObject, r);
        }
    }

    public List<Repository> read() {
        List<Repository> repositories = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get("repositories.txt"))) {
            stream.forEach((String name) -> {
                String[] names = name.split(",");
                Repository r = new Repository(names[0], names[1]);
                repositories.add(r);
            });
        } catch (IOException ex) {
            System.out.println("Error in read repositories names: " + ex.getMessage());
        }
        return repositories;
    }

    public boolean hasNext(JSONObject jsonObject) {
        try {
            if (!statusProject.isCompleteIssue()) {
                this.hasNextIssue = HasNext.getIssueEndCursor(jsonObject);
                statusProject.setEndCursorIssue(hasNextIssue.getEndCursor());
            }
            if (!statusProject.isCompletePull()) {
                this.hasNextPull = HasNext.getPullEndCursor(jsonObject);
                statusProject.setEndCursorPull(hasNextPull.getEndCursor());
            }

            if (!this.hasNextPull.isHasNext()) {
                statusProject.setCompletePull(true);
            }
            if (!this.hasNextIssue.isHasNext()) {
                statusProject.setCompleteIssue(true);
            }

        } catch (JSONException ex) {
            System.out.println("Error in read next end cursor: " + ex.getMessage());
        }
        this.statusProject = statusProject.save();
        return hasNextPull.isHasNext() || hasNextIssue.isHasNext();
    }

    public void getRepositories(List<Repository> repositories) throws JSONException {
        for (Repository r : repositories) {
            System.out.println("Extract " + r.getOwner() + " - " + r.getName());
            this.statusProject = Status.find(r.getOwner(), r.getName());
            if (this.statusProject == null) {
                this.statusProject = new Status(r.getOwner(), r.getName());
                this.hasNextIssue = new HasNext(true, "");
                this.hasNextPull = new HasNext(true, "");

                this.query = getQuery(r);
            } else {
                this.hasNextIssue = new HasNext(!this.statusProject.isCompleteIssue(), this.statusProject.getEndCursorIssue());
                this.hasNextPull = new HasNext(!this.statusProject.isCompletePull(), this.statusProject.getEndCursorPull());
                this.query = getQueryEndCursor(r);
            }
            if(!this.statusProject.isCompleteIssue() || !this.statusProject.isCompletePull()){
              this.extract(r);  
            }
            
        }
    }

    public static void main(String[] args) throws Exception {
        Request r = new Request();
        List<Repository> repositories = r.read();
        r.getRepositories(repositories);
    }
    
    public JSONObject getHttpClient(String query) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        // Create some NameValuePair for HttpPost parameters
        Header header[] = new BasicHeader[2];
        header[0] = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        header[1] = new BasicHeader("Authorization", "bearer " + tokens[tokenNow]);
        post.setHeaders(header);
        try {
            post.setEntity(new StringEntity(query));
            HttpResponse response = client.execute(post);

            // Print out the response message
            String jsonResponse = EntityUtils.toString(response.getEntity());
            try {
                JSONObject o = new JSONObject(jsonResponse);
                if (verifyLimitNow(o)) {
                    if (tokenNow == (tokens.length - 1)) {
                        tokenNow = 0;
                    } else {
                        tokenNow = tokenNow + 1;
                    }
                }
                if (o.isNull("data")) {
                    System.out.println("Data is null, query: " + jsonResponse);
                    statusProject.setErrorResponse(jsonResponse);
                    statusProject.setErrorQuery(this.query);
                    statusProject = statusProject.save();
                    return null;
                }
                if (o.getJSONObject("data").isNull("repository")) {
                    System.out.println("Data is null, query: " + jsonResponse);
                    statusProject.setErrorResponse(jsonResponse);
                    statusProject.setErrorQuery(this.query);
                    statusProject = statusProject.save();
                    return null;
                }
                tentativas = 0;
                return o;
            } catch (JSONException ex) {
                System.out.println("Error in get object json extract: " + ex.getMessage());
                statusProject.setError("Error in get object json extract: " + ex.getMessage());
                statusProject.setErrorResponse(jsonResponse);
                statusProject.setErrorQuery(this.query);
                statusProject = statusProject.save();
                tentativas = tentativas+1;
                if(tentativas == 3){
                    tentativas = 0;
                    return null;
                }
                return getHttpClient(query);
            }

        } catch (org.apache.http.NoHttpResponseException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return null;
    }

}
