/*
 */
package br.edu.ufsm.model.event;

import br.edu.ufsm.model.Issue;
import br.edu.ufsm.model.Milestone;
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

@Entity
@Table(name = "EventIssue")
public @Data
class EventIssue implements Serializable, EntityBD {

    @Id
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private TypeEventIssue type;
    private String url;
    private User actor;
    private String commitId;
    private String event;
    private Date createdAt;
    private String label;
    @ManyToOne(cascade = CascadeType.ALL)
    private User assignee;
    @ManyToOne(cascade = CascadeType.ALL)
    private User assigner;
    @ManyToOne(cascade = CascadeType.ALL)
    private Milestone milestone;
    private String rename;
    @ManyToOne(cascade = CascadeType.ALL)
    private Issue issue;

    public EventIssue() {

    }

    public EventIssue(org.eclipse.egit.github.core.IssueEvent event) {
        this.id = event.getId();
        this.url = event.getUrl();
        this.actor = new User(event.getActor());
        this.commitId = event.getCommitId();
        this.event = event.getEvent();
        this.createdAt = event.getCreatedAt();
        if(event.getLabel() != null){
            
        }
        
        this.assignee = new User(event.getAssignee());
        this.assigner = new User(event.getAssigner());
        this.milestone = new Milestone(event.getMilestone());
        if(event.getRename() != null){
            this.rename = event.getRename().getFrom() + " to " + event.getRename().getTo();
        }
        this.type = TypeEventIssue.getType(event.getEvent());
    }

    /**
     * @return the pk
     */
    @Override
    public Object getPk() {
        return this.id;
    }
}
