/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import br.edu.ufsm.persistence.EntityBD;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author douglas
 */
@Entity
@Table(name = "mailinglist_message")
public class GoogleGroupMessage implements Serializable, EntityBD {

    @Id
    private String id;
    @ManyToOne(cascade = CascadeType.ALL)
    private GoogleGroupUser user;
    @Lob
    @Column(length = 10000)
    private String message;
    private Date date;
    @ManyToOne(cascade = CascadeType.ALL)
    private GoogleGroupTopic topic;
    @Embedded
    private Sentimento sentimento;
    
    public GoogleGroupMessage(){
        this.sentimento = new Sentimento();
    }
    
    /**
     * @return the id
     */
    @Override
    public Object getPk() {
        return getId();
    }

    /**
     * @return the user
     */
    public GoogleGroupUser getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(GoogleGroupUser user) {
        this.user = user;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the topic
     */
    public GoogleGroupTopic getTopic() {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(GoogleGroupTopic topic) {
        this.topic = topic;
    }

    /**
     * @return the sentimento
     */
    public Sentimento getSentimento() {
        return sentimento;
    }

    /**
     * @param sentimento the sentimento to set
     */
    public void setSentimento(Sentimento sentimento) {
        this.sentimento = sentimento;
    }
}
