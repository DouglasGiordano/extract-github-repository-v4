/*
 * To change this license header, choose License Headers in Issue Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.GoogleGroup;
import br.edu.ufsm.model.Project;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Douglas Giordano
 */
@Stateful
@LocalBean
public class GoogleGroupDao extends NewPersistence<GoogleGroup, Integer> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public GoogleGroupDao() {

    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new GoogleGroup();
    }

    @Override
    public GoogleGroup getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }

    public List<GoogleGroup> getGroups(Project project) {
        return listByCriteria("project", project, GoogleGroup.class);
    }

    @Transactional()
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(Collection<GoogleGroup> entities) {
        EntityManager em = entityManager;
        int i = 0;
        int batchSize = 50;
        for (GoogleGroup group : entities) {
            try {
                em.merge(group);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(group.getName());
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
    public GoogleGroup merge(GoogleGroup myEntity) {
        EntityManager em = getEntity();
        GoogleGroup entity = em.merge(myEntity);
        return myEntity;
    }
}
