/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.requestpostgraphql;

import br.edu.ufsm.requestpostgraphql.entity.PullRequest;
import br.edu.ufsm.requestpostgraphql.entity.Status;
import br.edu.ufsm.requestpostgraphql.entity.Complex;
import br.edu.ufsm.requestpostgraphql.entity.HasNext;
import br.edu.ufsm.requestpostgraphql.entity.Issue;
import br.edu.ufsm.requestpostgraphql.entity.Repository;
import br.edu.ufsm.requestpostgraphql.service.BranchService;
import br.edu.ufsm.requestpostgraphql.service.CommitService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dougl
 */
public class Request {

    private String query = "";
    private HasNext hasNextPull = new HasNext(true, "");
    private HasNext hasNextIssue = new HasNext(true, "");
    private Status statusProject;

    private CommitService commitService = new CommitService();
    private BranchService branchService = new BranchService();

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
//        JSONObject jsonResponse = this.getHttpClient(this.query, statusProject);
        JSONObject jsonResponse = null;
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

    public void getRepositoriesIssuePull(List<Repository> repositories) throws JSONException {
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
            if (!this.statusProject.isCompleteIssue() || !this.statusProject.isCompletePull()) {
                this.extract(r);
            }

        }
    }

    public void getRepositoriesBranch(List<Repository> repositories) throws JSONException {
        for (Repository r : repositories) {
            System.out.println("Extract branch " + r.getOwner() + " - " + r.getName());
            branchService.extractBranch(r);
        }
    }

    public void getRepositoriesCommit(List<Repository> repositories) throws JSONException {
        for (Repository r : repositories) {
            System.out.println("Extract commit " + r.getOwner() + " - " + r.getName());
            commitService.extractCommit(r);
        }
    }

    public static void main(String[] args) throws Exception {
        Request r = new Request();
        List<Repository> repositories = r.read();
        int option = 0;
        while (option != 4) {
            System.out.println("Digite: ");
            System.out.println("1 - Issue e Pull | 2 - Branchs | 3 - Commits | 4 - Sair");
            Scanner reader = new Scanner(System.in);
            option = reader.nextInt();
            switch (option) {
                case 1:
                    r.getRepositoriesIssuePull(repositories);
                    break;
                case 2:
                    r.getRepositoriesBranch(repositories);
                    break;
                case 3:
                    r.getRepositoriesCommit(repositories);
                    break;
                case 4:
                    System.exit(1);
            }
        }
    }

}
