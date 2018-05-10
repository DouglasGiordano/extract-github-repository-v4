/*
 */
package br.edu.ufsm.controller;

import br.edu.ufsm.model.Issue;
import br.edu.ufsm.model.Project;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dougl
 */
public class ExtracaoIssue {

    public static List<Issue> extract(GitHubClient client, RepositoryId repositoryId, Project project) throws IOException {
        List<Issue> list = null;
            IssueService serviceIssue = new IssueService(client);
            Map<String, String> filtros = new HashMap<>();
            filtros.put("state", "all");
            filtros.put("filter", "all");
            List<org.eclipse.egit.github.core.Issue> issues = serviceIssue.getIssues(repositoryId,
                    filtros);
            list = new LinkedList<>();
            for (org.eclipse.egit.github.core.Issue issue : issues) {
                    list.add(new Issue(issue, project));
            }
        return list;
    }
}
