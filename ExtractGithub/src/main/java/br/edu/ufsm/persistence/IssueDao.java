/*
 * To change this license header, choose License Headers in Issue Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Issue;
import br.edu.ufsm.model.Project;
import br.edu.ufsm.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Douglas Giordano
 */
@Stateful
@LocalBean
public class IssueDao extends NewPersistence<Issue, Integer> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public IssueDao() {

    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new Issue();
    }

    @Override
    public Issue getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }

    public List<Long> getIssues(long idProject) {
        Query q = getEntity().createNativeQuery("SELECT DISTINCT(issue.id) "
                + "FROM issue "
                + "LEFT JOIN issue_comment ON issue.id = issue_comment.issue_id where isnull(issue_comment.id) and project_id = " + idProject + "");
        return q.getResultList();
    }

    public List<Long> getIssuesAll(long idProject) {
        Query q = getEntity().createNativeQuery("SELECT DISTINCT(issue.id) "
                + "FROM issue "
                + "where project_id = " + idProject + "");
        return q.getResultList();
    }

    public List<Long> getIssuesId(long idProject) {
        Query q = getEntity().createNativeQuery("SELECT DISTINCT(issue.id) "
                + "FROM issue_comment "
                + "INNER JOIN issue ON issue.id = issue_comment.issue_id where project_id = " + idProject);
        return q.getResultList();
    }
    public List<BigInteger> getIssuesUser(long idProject, User usuario) {
        Query q = getEntity().createNativeQuery("SELECT DISTINCT(issue.id) "
                + "FROM issue_comment "
                + "INNER JOIN issue ON issue.id = issue_comment.issue_id where project_id = " + idProject + " and issue_comment.user_id = " + usuario.getId());
        return q.getResultList();
    }

    public Integer getLastId(long idProject) {
        Query q = getEntity().createNativeQuery("SELECT MIN(number) FROM issue where issue.project_id = " + idProject);
        return (Integer) q.getSingleResult();
    }

    public List<Object[]> getRede(long idProject) {
//        Query q = getEntity().createNativeQuery("SELECT GROUP_CONCAT(concat(issue_comment.user_id,'(',user.login,')') SEPARATOR '##'), issue.id\n" +
        Query q = getEntity().createNativeQuery("SELECT GROUP_CONCAT(issue_comment.user_id SEPARATOR '-'), issue.id\n"
                + "                FROM extract_github.issue_issue_comment\n"
                + "                INNER JOIN issue ON issue.id = issue_issue_comment.issue_id\n"
                + "                INNER JOIN issue_comment ON issue_comment.id = issue_issue_comment.issueComments_id\n"
                + "				INNER JOIN project_issue ON issue.id = project_issue.issue_id\n"
                + "                INNER JOIN extract_github.user ON user.id = issue_comment.user_id\n"
                + "                WHERE project_issue.project_id = " + idProject + " "
                + "                GROUP BY issue_issue_comment.issue_id");
        return q.getResultList();
    }

    @Transactional()
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(Collection<Issue> entities) {
        EntityManager em = entityManager;
        int i = 0;
        int batchSize = 50;
        for (Issue issue : entities) {
            try {
                em.merge(issue);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(issue.getNumber());
            i++;
            if (i % batchSize == 0) {
                // Flush a batch of inserts and release memory.
                try {
                    em.flush();
                    em.clear();
                } catch (Exception ex) {
                    System.out.println("Erro " + i + " " + ex.getMessage());
                }

            }
        }
    }

    public Issue merge(Issue myEntity) {
        EntityManager em = getEntity();
        Issue entity = em.merge(myEntity);
        return myEntity;
    }


    public Long count(Project project){
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<Issue> from = cq.from(Issue.class);
        cq.select(qb.count(from));
        cq.where(qb.equal(from.get("project"), project));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
