/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.menagedbeans;

import br.edu.ufsm.model.Settings;
import br.edu.ufsm.model.UserAutenticacao;
import br.edu.ufsm.persistence.UsuarioAutenticacaoDao;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import uk.ac.wlv.sentistrength.SentiStrength;

/**
 *
 * @author Douglas Giordano
 */
@Named(value = "settingsbean")
@ViewScoped
public class SettingsMBean implements Serializable {

    @EJB
    private UsuarioAutenticacaoDao userDao;
    private UserAutenticacao user;
    private List users;
    private Settings settings;

    /**
     * Creates a new instance of ExtrairMBean
     */
    @PostConstruct
    public void init() {
        user = new UserAutenticacao();
        users = userDao.findAll(UserAutenticacao.class);
        setSettings(new Settings());
    }

    public void testar() {
        SentiStrength sentiStrength = new SentiStrength();
        System.out.println(this.settings.getPath());
        String ssthInitialisation[] = {"sentidata", this.settings.getPath(), "explain"};

        sentiStrength.initialise(ssthInitialisation);
    }

    public void adicionar() {
        try {
            userDao.save(this.user);
            users.add(user);
            user = new UserAutenticacao();
            MessageView.showMessage("Registro Armazenado", "Usuário " + this.user.getUsuario() + " foi armazenado.");
        } catch (Exception ex) {
            MessageView.showMessageDanger("Registro Não Armazenado", "Usuário " + this.user.getUsuario() + " não foi armazenado.");
            MessageView.showMessageDanger("Erro", ex.getMessage());
        }
    }

    public void remover(UserAutenticacao usuario) {
        try {
            userDao.remove(usuario);
            users = userDao.findAll();
            MessageView.showMessage("Registro Removido", "Usuário " + this.user.getUsuario() + " foi removido.");
        } catch (Exception ex) {
            MessageView.showMessageDanger("Registro Não Removido", "Usuário " + this.user.getUsuario() + " não foi removido.");
            MessageView.showMessageDanger("Erro", ex.getMessage());
        }
    }

    public void editar(UserAutenticacao usuario) {
        this.user = usuario;
    }

    /**
     * @return the userDao
     */
    public UsuarioAutenticacaoDao getUserDao() {
        return userDao;
    }

    /**
     * @param userDao the userDao to set
     */
    public void setUserDao(UsuarioAutenticacaoDao userDao) {
        this.userDao = userDao;
    }

    /**
     * @return the user
     */
    public UserAutenticacao getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserAutenticacao user) {
        this.user = user;
    }

    /**
     * @return the users
     */
    public List<UserAutenticacao> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(ArrayList<UserAutenticacao> users) {
        this.users = users;
    }

    /**
     * @return the settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * @param settings the settings to set
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

}
