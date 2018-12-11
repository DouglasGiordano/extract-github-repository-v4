/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.requestpostgraphql.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Douglas Giordano
 */
public class PersistenceGithub {
    private static EntityManagerFactory entityManagerFactory;
    /**
     * Open entity manager.
     *
     * @return the entity manager
     */
    public static EntityManager openEntityManager() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("demo");
        }
        return entityManagerFactory.createEntityManager();
    }
}
