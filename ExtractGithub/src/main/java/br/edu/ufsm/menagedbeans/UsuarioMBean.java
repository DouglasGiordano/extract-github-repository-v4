/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.menagedbeans;

import br.edu.ufsm.model.User;
import br.edu.ufsm.persistence.UsuarioDao;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Douglas Giordano
 */
@Named("usuarioMBean")
@ViewScoped
public class UsuarioMBean implements Serializable {

    @EJB
    private UsuarioDao dao;
    private List<User> usuarios;
    
    
    public void atualizarUsuarios(){
        for(User u: usuarios){
            
        }
    }
    
    /**
     * Creates a new instance of ExtrairMBean
     */
    @PostConstruct
    public void init() {
        this.usuarios = dao.findAll(User.class);
    }

    /**
     * @return the usuarios
     */
    public List<User> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(ArrayList<User> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * @return the dao
     */
    public UsuarioDao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(UsuarioDao dao) {
        this.dao = dao;
    }

}
