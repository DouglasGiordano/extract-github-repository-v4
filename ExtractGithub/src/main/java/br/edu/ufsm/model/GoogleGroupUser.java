/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

/**
 *
 * @author douglas
 */
@Entity
@Table(name = "mailinglist_user", uniqueConstraints =
@UniqueConstraint(columnNames = "name"))
public class GoogleGroupUser implements Serializable {

    @Id
    private String name;

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
