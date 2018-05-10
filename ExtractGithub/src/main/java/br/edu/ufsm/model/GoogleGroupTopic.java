/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import br.edu.ufsm.persistence.EntityBD;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author douglas
 */
@Entity
@Table(name = "mailinglist_topic")
public class GoogleGroupTopic implements Serializable, EntityBD {

    @Id
    @Column(name = "url", unique = true, nullable = false, length = 500)
    private String url;
    private String title;
    @ManyToOne(cascade = CascadeType.ALL)
    private GoogleGroup group;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "topic", cascade = CascadeType.ALL)
    private List<GoogleGroupMessage> messages;

    @Override
    public Object getPk() {
        return getUrl();
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
     * @return the group
     */
    public GoogleGroup getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(GoogleGroup group) {
        this.group = group;
    }

    /**
     * @return the messages
     */
    public List<GoogleGroupMessage> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<GoogleGroupMessage> messages) {
        this.messages = messages;
    }
}
