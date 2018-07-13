/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Douglas Giordano
 */
@Entity
@Table(name = "milestone")
public class Milestone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueOn;
    private int closedIssues;
    private int number;
    private int openIssues;
    @Column(length = 300)
    private String description;
    private String state;
    private String title;
    private String url;
    @ManyToOne(cascade = CascadeType.ALL)
    private User creator;

    public Milestone(org.eclipse.egit.github.core.Milestone milestone) {
        if (milestone == null) {
            return;
        }
        this.createdAt = milestone.getCreatedAt();
        this.dueOn = milestone.getDueOn();
        this.closedIssues = milestone.getClosedIssues();
        this.number = milestone.getNumber();
        this.openIssues = milestone.getOpenIssues();
        this.description = milestone.getDescription();
        this.state = milestone.getState();
        this.title = milestone.getTitle();
        this.url = milestone.getUrl();
        this.creator = new User(milestone.getCreator());
    }

    public Milestone() {

    }

    /**
     * @return the createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the dueOn
     */
    public Date getDueOn() {
        return dueOn;
    }

    /**
     * @param dueOn the dueOn to set
     */
    public void setDueOn(Date dueOn) {
        this.dueOn = dueOn;
    }

    /**
     * @return the closedIssues
     */
    public int getClosedIssues() {
        return closedIssues;
    }

    /**
     * @param closedIssues the closedIssues to set
     */
    public void setClosedIssues(int closedIssues) {
        this.closedIssues = closedIssues;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the openIssues
     */
    public int getOpenIssues() {
        return openIssues;
    }

    /**
     * @param openIssues the openIssues to set
     */
    public void setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the creator
     */
    public User getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(User creator) {
        this.creator = creator;
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
