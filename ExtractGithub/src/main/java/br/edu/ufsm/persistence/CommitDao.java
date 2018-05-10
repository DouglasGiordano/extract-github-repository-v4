/*
 * To change this license header, choose License Headers in Commit Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Commit;
import br.edu.ufsm.model.Project;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

/**
 * @author Douglas Giordano
 */
@Stateful
@LocalBean
public class CommitDao extends NewPersistence<Commit, String> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public CommitDao() {

    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new Commit();
    }

    @Override
    public Commit getObject() {
        return this.object;
    }

    public List<String> getCommits(long idProject) {
        Query q = getEntity().createNativeQuery("SELECT DISTINCT(commit.sha) "
                + "FROM commit "
                + "LEFT JOIN commit_file ON commit.sha = commit_file.commit_sha where isnull(commit_file.id) and project_id = " + idProject + "");
        return q.getResultList();
    }

    public List<String> getCommitsId(long idProject) {
        Query q = getEntity().createNativeQuery("SELECT DISTINCT(commit.sha) "
                + "FROM commit where project_id = " + idProject + "");
        return q.getResultList();
    }

    public int getTotalCommits(Project projeto) {
        CriteriaBuilder builder = getEntity().getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(Commit.class);
        query.select(getEntity().getCriteriaBuilder().count(from));
        query.where(builder.equal(from.get("project").get("id"), projeto.getId()));
        TypedQuery qB = getEntity().createQuery(query);
        Integer totalResult = ((Long) qB.getSingleResult()).intValue();
        return totalResult;
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }

    public void save(Collection<Commit> entities) {
        for (Commit commit : entities) {
            entityManager.persist(commit);
        }
        entityManager.clear();
    }

    public Commit merge(Commit commit) {
        entityManager.merge(commit);
        return commit;
    }

    public Long count(Project project){
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<Commit> from = cq.from(Commit.class);
        cq.select(qb.count(from));
        cq.where(qb.equal(from.get("project"), project));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
