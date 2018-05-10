/*
 * To change this license header, choose License Headers in Issue Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Project;
import br.edu.ufsm.model.PullRequest;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

/**
 *
 * @author Douglas Giordano
 */
@Stateful
@LocalBean
public class PullRequestDao extends NewPersistence<PullRequest, Integer> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public PullRequestDao() {

    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new PullRequest();
    }

    @Override
    public PullRequest getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }


    @Transactional()
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PullRequest merge(PullRequest myEntity) {
        EntityManager em = getEntity();
        PullRequest entity = em.merge(myEntity);
        return myEntity;
    }


    public Long count(Project project){
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<PullRequest> from = cq.from(PullRequest.class);
        cq.select(qb.count(from));
        cq.where(qb.equal(from.get("project"), project));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
