/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import br.edu.ufsm.persistence.EntityBD;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author douglas
 */
@Entity
@Table(name = "user_autenticacao")
public class UserAutenticacao implements Serializable, EntityBD{

    @Id
    private String usuario;
    private String senha;

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public Object getPk() {
        return this.usuario;
    }
}
