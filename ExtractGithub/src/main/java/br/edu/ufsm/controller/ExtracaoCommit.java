/*
 */
package br.edu.ufsm.controller;

import br.edu.ufsm.model.Commit;
import br.edu.ufsm.model.Project;
import br.edu.ufsm.persistence.CommitCommentDao;
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
import org.eclipse.egit.github.core.CommitComment;

/**
 *
 * @author Douglas Montanha Giordano
 */
public class ExtracaoCommit {

    public static List<Commit> extract(GitHubClient client, RepositoryId repositoryId, Project project) throws IOException {
        List<Commit> list = new LinkedList<>();
        //Basic authentication
        CommitService commitService = new CommitService(client);
        List<RepositoryCommit> commits = commitService.getCommits(repositoryId);
        for (RepositoryCommit commit : commits) {
            list.add(new Commit(commit, project));
        }
        return list;
    }

    public static void extractComments(GitHubClient client, RepositoryId repositoryId, List<String> commits, CommitCommentDao dao) {
        //Basic authentication
        CommitService commitService = new CommitService(client);
        for (String idCommit : commits) {
            List<CommitComment> comments = null;
            try {
                comments = commitService.getComments(repositoryId, idCommit);
            } catch (IOException ex) {
                System.out.println("Error in get comments of commit: "+ ex.getMessage());
            }
            comments.stream().map((comentario) -> {
                Commit commit = new Commit();
                commit.setSha(idCommit);
                br.edu.ufsm.model.CommitComment c = new br.edu.ufsm.model.CommitComment(comentario, commit);
                return c;
            }).forEachOrdered((c) -> {
                dao.save(c);
            });
        }
    }

}
