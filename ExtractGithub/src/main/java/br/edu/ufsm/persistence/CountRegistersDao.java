/*
 * To change this license header, choose License Headers in Issue Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.CountRegisters;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Douglas Giordano
 */
@Stateful
@LocalBean
public class CountRegistersDao {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public CountRegistersDao() {

    }

    public EntityManager getEntity() {
        return this.entityManager;
    }

    public CountRegisters getCount(){
        CriteriaQuery cq = getEntity().getCriteriaBuilder().createQuery();
        cq.select(cq.from(CountRegisters.class));
        TypedQuery<CountRegisters> qB = getEntity().createQuery(cq);
        try {
            return qB.setMaxResults(1).getSingleResult();
        } catch (NoResultException ex) {
            return new CountRegisters();
        }
    }
}
