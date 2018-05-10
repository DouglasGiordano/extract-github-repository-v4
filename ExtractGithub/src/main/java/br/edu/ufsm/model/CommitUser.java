/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author douglas montanha giordano
 */
@Embeddable
public class CommitUser implements Serializable {

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date date;
    private String email;
    private String name;

    public CommitUser() {

    }

    public CommitUser(org.eclipse.egit.github.core.CommitUser commitUser) {
        this.date = commitUser.getDate();
        this.email = commitUser.getEmail();
        this.name = commitUser.getName();
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
