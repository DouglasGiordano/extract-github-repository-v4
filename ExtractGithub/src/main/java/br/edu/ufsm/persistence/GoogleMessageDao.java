/*
 * To change this license header, choose License Headers in Issue Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.GoogleGroup;
import br.edu.ufsm.model.GoogleGroupMessage;
import br.edu.ufsm.model.GoogleGroupTopic;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
import java.util.List;

/**
 *
 * @author Douglas Giordano
 */
@Stateful
@LocalBean
public class GoogleMessageDao extends NewPersistence<GoogleGroupMessage, Integer> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public GoogleMessageDao() {

    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new GoogleGroupMessage();
    }

    @Override
    public GoogleGroupMessage getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }

    public List<GoogleGroupTopic> getGroupTopics(GoogleGroup group) {
        return listByCriteria("group", group, GoogleGroupTopic.class);
    }

    @Transactional()
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(List<GoogleGroupMessage> entities) {
        EntityManager em = getEntity();

        int i = 0;
        int batchSize = 40;
        for (GoogleGroupMessage m : entities) {
            em.merge(m);
            i++;
            if (i % batchSize == 0) {
                // Flush a batch of inserts and release memory.
                try {
                    em.flush();
                    em.clear();
                    System.out.println(i);
                } catch (Exception ex) {
                    System.out.println("Erro " + i + " " + ex.getMessage());
                }
            }

        }
        em.flush();
        em.clear();
    }

    public GoogleGroupMessage merge(GoogleGroupMessage myEntity) {
        EntityManager em = getEntity();
        GoogleGroupMessage entity = em.merge(myEntity);
        return myEntity;
    }
}
