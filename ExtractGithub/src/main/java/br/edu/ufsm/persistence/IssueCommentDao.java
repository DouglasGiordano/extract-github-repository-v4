/*
 * To change this license header, choose License Headers in IssueComment Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.IssueComment;

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
public class IssueCommentDao extends NewPersistence<IssueComment, Integer> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public IssueCommentDao() {

    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new IssueComment();
    }

    @Override
    public IssueComment getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }

    public List<Object> getUsuariosEmComum(long idProject, long idProject2) {
        Query q = getEntity().createNativeQuery("SELECT DISTINCT(user.id) FROM user\n"
                + "INNER JOIN issue_comment ON issue_comment.user_id = user.id\n"
                + "WHERE user.id IN \n"
                + " (SELECT issue_comment.user_id FROM issue_comment \n"
                + " INNER JOIN issue ON issue.id = issue_comment.issue_id\n"
                + "  where issue.project_id = "+idProject+") and issue_comment.user_id IN \n"
                + " (SELECT issue_comment.user_id FROM issue_comment \n"
                + " INNER JOIN issue ON issue.id = issue_comment.issue_id\n"
                + "  where issue.project_id = "+idProject2+")");
        return q.getResultList();
    }

}
