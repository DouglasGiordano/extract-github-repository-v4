/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.menagedbeans;

import br.edu.ufsm.model.CountRegisters;
import br.edu.ufsm.persistence.CountRegistersDao;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author Douglas Giordano
 */
@Named(value = "pagebean")
@ApplicationScoped
public class PageMBean implements Serializable {
    @EJB
    private CountRegistersDao coutRegisterDao;
    private CountRegisters countRegisters;

    @PostConstruct
    public void init(){
        this.countRegisters = coutRegisterDao.getCount();
    }

    public CountRegisters getCountRegisters(){
        return countRegisters;
    }

    public String getUsuario() {
        return "user?faces-redirect=true";
    }
}
