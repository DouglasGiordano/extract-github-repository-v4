/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.requestpostgraphql;

import br.edu.ufsm.requestpostgraphql.entity.Message;
import br.edu.ufsm.requestpostgraphql.entity.Repository;
import br.edu.ufsm.requestpostgraphql.service.BranchService;
import br.edu.ufsm.requestpostgraphql.service.CommitService;
import br.edu.ufsm.requestpostgraphql.service.LoadManagement;
import br.edu.ufsm.requestpostgraphql.service.PullIssueService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;
import org.json.JSONException;

/**
 *
 * @author Dougl
 */
public class Request {

    private final CommitService commitService = new CommitService();
    private final BranchService branchService = new BranchService();
    private final PullIssueService pullIssueService = new PullIssueService();

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

    public void getRepositoriesIssuePull(List<Repository> repositories) throws JSONException {
        for (Repository r : repositories) {
            LoadManagement.getInstance().init();
            System.out.println("Extract pull and issues " + r.getOwner() + " - " + r.getName());
            pullIssueService.extractPullIssue(r);
        }
    }

    public void getRepositoriesBranch(List<Repository> repositories) throws JSONException {
        for (Repository r : repositories) {
            LoadManagement.getInstance().init();
            System.out.println("Extract branch " + r.getOwner() + " - " + r.getName());
            branchService.extractBranch(r);
        }
    }

    public void getRepositoriesCommit(List<Repository> repositories) throws JSONException {
        for (Repository r : repositories) {
            LoadManagement.getInstance().init();
            System.out.println("Extract commit " + r.getOwner() + " - " + r.getName());
            commitService.extractCommit(r);
        }
    }

    public void getRepositoriesCommitBranchDefault(List<Repository> repositories) throws JSONException {
        for (Repository r : repositories) {
            LoadManagement.getInstance().init();
            System.out.println("Extract commit branch default " + r.getOwner() + " - " + r.getName());
            commitService.extractCommitBranchDefault(r);
        }
    }

    public static void main(String[] args) throws Exception {
        Request r = new Request();
        List<Repository> repositories = r.read();
        int option = 0;
        while (option != 4) {
            System.out.println("Digite: ");
            System.out.println("1 - Issue e Pull | 2 - Branchs | 3 - Commits | 4 - Commits Branch Default | 0 - Sair");
            Scanner reader = new Scanner(System.in);
            option = reader.nextInt();
            try {
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
                        r.getRepositoriesCommitBranchDefault(repositories);
                        break;
                    case 0:
                        System.exit(1);
                }
            } catch (org.hibernate.service.spi.ServiceException ex) {
                Message.printError("Configuration is not working: " + ex.getMessage());
            }
        }
    }

}
