/*
 */
package br.edu.ufsm.controller;

import br.edu.ufsm.model.Project;
import br.edu.ufsm.model.PullRequest;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.PullRequestService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dougl
 */
public class ExtracaoPullRequest {

    public static List<PullRequest> extract(GitHubClient client, RepositoryId repositoryId, Project project) {
        List<PullRequest> list = new ArrayList<>();
        try {
            PullRequestService serviceIssue = new PullRequestService(client);
            Map<String, String> filtros = new HashMap<>();
            filtros.put("state", "all");
            List<org.eclipse.egit.github.core.PullRequest> pullRequests = serviceIssue.getPullRequests(repositoryId, "");
            for (org.eclipse.egit.github.core.PullRequest pull : pullRequests) {
                list.add(new PullRequest(pull, project));
            }
        } catch (IOException ex) {
            Logger.getLogger(ExtracaoPullRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
