/*
 * To change this license header, choose License Headers in Issue Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.GoogleGroupTopic;
import br.edu.ufsm.model.Project;

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
import java.util.Collection;

/**
 *
 * @author Douglas Giordano
 */
@Stateful
@LocalBean
public class GoogleGroupTopicDao extends NewPersistence<GoogleGroupTopic, Integer> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public GoogleGroupTopicDao() {

    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new GoogleGroupTopic();
    }

    @Override
    public GoogleGroupTopic getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }

    @Transactional()
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(Collection<GoogleGroupTopic> entities) {
        EntityManager em = entityManager;
        int i = 0;
        int batchSize = 50;
        for (GoogleGroupTopic group : entities) {
            try {
                em.merge(group);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(group.getUrl());
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

    @Transactional()
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public GoogleGroupTopic merge(GoogleGroupTopic myEntity) {
        EntityManager em = getEntity();
        GoogleGroupTopic entity = em.merge(myEntity);
        return myEntity;
    }


    public Long count(Project project){
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<GoogleGroupTopic> from = cq.from(GoogleGroupTopic.class);
        cq.select(qb.count(from));
        cq.where(qb.equal(from.get("group").get("project"), project));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
