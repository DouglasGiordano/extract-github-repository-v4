/*
 */
package br.edu.ufsm.controller;

import br.edu.ufsm.model.Commit;
import br.edu.ufsm.model.Project;
import br.edu.ufsm.persistence.CommitDao;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Douglas Montanha Giordano
 */
public class ExtracaoCommit {

    public static List<Commit> extract(GitHubClient client, RepositoryId repositoryId, Project project) {
        List<Commit> list = new LinkedList<>();
        try {
            //Basic authentication
            CommitService commitService = new CommitService(client);
            List<RepositoryCommit> commits = commitService.getCommits(repositoryId);
            for (RepositoryCommit commit : commits) {
               list.add(new Commit(commit, project));
            }
        } catch (IOException ex) {
            Logger.getLogger(ExtracaoIssue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static void extract(GitHubClient client, RepositoryId repositoryId, List<String> commits, CommitDao commitDao, Project project) {
        //Basic authentication
        CommitService commitService = new CommitService(client);
        Commit commitD;
        RepositoryCommit commit;
        for (String idCommit : commits) {
            try {
                commit = commitService.getCommit(repositoryId, idCommit);
                commitD = new Commit(commit, project);
                commitDao.merge(commitD);
                Logger.getGlobal().info(commitD.getSha());
            } catch (Exception ex) {
                Logger.getGlobal().info("Impossivel baixar commit: " + idCommit + "\n\n" + ex.getMessage());
            } catch (OutOfMemoryError ex) {
                Logger.getGlobal().info("Estouro de Memória: " + idCommit + "\n\n" + ex.getMessage());
            }
        }
    }
    
}
