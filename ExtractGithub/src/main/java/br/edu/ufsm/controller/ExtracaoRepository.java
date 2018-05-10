/*
 */
package br.edu.ufsm.controller;

import br.edu.ufsm.model.Project;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Douglas Montanha Giordano
 */
public class ExtracaoRepository {

    public static Project extractRepository(GitHubClient client, RepositoryId repositoryId) {
        Repository project = null;
        try {
            //Basic authentication
            RepositoryService service = new RepositoryService(client);
            project = service.getRepository(repositoryId.getOwner(), repositoryId.getName());
        } catch (IOException ex) {
            Logger.getLogger(ExtracaoIssue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Project(project);
    }
}
