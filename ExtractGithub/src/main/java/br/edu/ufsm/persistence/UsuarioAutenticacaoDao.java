/*
 * To change this license header, choose License Headers in User Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.UserAutenticacao;

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
public class UsuarioAutenticacaoDao extends NewPersistence<UserAutenticacao, String> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public UsuarioAutenticacaoDao() {

    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new UserAutenticacao();
    }

    @Override
    public UserAutenticacao getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }

}
