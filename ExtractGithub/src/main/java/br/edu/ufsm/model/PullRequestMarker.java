/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Douglas Giordano
 */
@Entity
@Table(name = "pull_request_marker")
public class PullRequestMarker implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String sha;
    private String label;
    private String ref;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public PullRequestMarker(org.eclipse.egit.github.core.PullRequestMarker pullMarker){
        if(pullMarker == null){
            return;
        }
        this.sha = pullMarker.getSha();
        this.label = pullMarker.getLabel();
        this.user = new User(pullMarker.getUser());
        this.ref = pullMarker.getRef();
    }
    
    public PullRequestMarker(){
        
    }
    
    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the ref
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param ref the ref to set
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
     * @return the sha
     */
    public String getSha() {
        return sha;
    }

    /**
     * @param sha the sha to set
     */
    public void setSha(String sha) {
        this.sha = sha;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
