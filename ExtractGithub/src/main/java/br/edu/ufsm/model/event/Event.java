/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model.event;

import br.edu.ufsm.model.User;
import br.edu.ufsm.persistence.EntityBD;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Dougl
 */
@Entity
@Table(name = "Event")
public @Data
class Event implements Serializable, EntityBD {

    @Enumerated(EnumType.STRING)
    @Column(length = 34)
    private TypeEvent type;
    private boolean isPublic;
    @Id
    private String id;
    @ManyToOne(cascade = CascadeType.ALL)
    private User actor;
    @ManyToOne(cascade = CascadeType.ALL)
    private User org;
    private Date createdAt;

    public Event() {
    }

    public Event(org.eclipse.egit.github.core.event.Event event) {
        this.isPublic = event.isPublic();
        this.id = event.getId();
        this.actor = new User(event.getActor());
        this.org = new User(event.getOrg());
        this.createdAt = event.getCreatedAt();
        this.type = TypeEvent.getType(event.getType());
    }

    /**
     * @return the pk
     */
    @Override
    public Object getPk() {
        return this.id;
    }
}
