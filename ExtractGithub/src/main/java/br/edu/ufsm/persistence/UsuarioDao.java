/*
 * To change this license header, choose License Headers in User Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author Douglas Giordano
 */
@Stateful
@LocalBean
public class UsuarioDao extends NewPersistence<User, Integer> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public UsuarioDao() {

    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new User();
    }

    @Override
    public User getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }

}
