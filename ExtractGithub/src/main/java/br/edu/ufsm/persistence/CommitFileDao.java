/*
 * To change this license header, choose License Headers in CommitFile Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.CommitFile;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * @author Douglas Giordano
 */
@Stateful
@LocalBean
public class CommitFileDao extends NewPersistence<CommitFile, Integer> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public CommitFileDao() {

    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new CommitFile();
    }

    @Override
    public CommitFile getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }

    public List<String> getUsersFile(long idProject) {
        Query q = getEntity().createNativeQuery(
                "SELECT GROUP_CONCAT(concat(commit.committer_id,':',DATE_FORMAT(commit.date_commiter_commit,'%Y-%d-%m')) SEPARATOR '##') "
                + "FROM extract_github.commit_commit_file "
                + "INNER JOIN commit ON commit.sha = commit_commit_file.commit_sha "
                + "INNER JOIN commit_file ON commit_file.id = commit_commit_file.files_id "
                + "		INNER JOIN project_commit ON commit.sha = project_commit.commits_sha "
                + "WHERE project_commit.project_id = " + Integer.parseInt(Long.toString(idProject)) + " "
                + "GROUP BY commit_file.filename "
        );
        return q.getResultList();
    }
}
