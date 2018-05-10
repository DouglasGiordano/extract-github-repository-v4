/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Project;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.HashMap;

/**
 *
 * @author Douglas Giordano
 */
@Stateful
@LocalBean
public class ProjectDao extends NewPersistence<Project, Object> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    @Override
    @PostConstruct
    public void init() {
        this.object = new Project();
    }

    @Override
    public Project getObject() {
        if (this.object == null) {
            return new Project();
        } else {
            return this.object;
        }
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }

    public Project getRepository(String owner, String name){
        HashMap<String, Object> filtros = new HashMap<>();
        filtros.put("owner.login", owner);
        filtros.put("name", name);
        return (Project) objectByCriteria(filtros, Project.class);
    }
}
