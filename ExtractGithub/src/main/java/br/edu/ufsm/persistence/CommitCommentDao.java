/*
 * To change this license header, choose License Headers in CommitComment Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Commit;
import br.edu.ufsm.model.CommitComment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class CommitCommentDao extends NewPersistence<CommitComment, Integer> {

    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public CommitCommentDao() {

    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new CommitComment();
    }

    @Override
    public CommitComment getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }
    
    
    public List<CommitComment> getComments(Commit commit){
        HashMap<String, Object> filtros = new HashMap<String, Object>();
        filtros.put("commit", commit);
        return this.listByCriteria(filtros, CommitComment.class);
    }
}
