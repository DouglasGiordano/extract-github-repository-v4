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
import lombok.Data;
import org.eclipse.egit.github.core.User;

/**
 *
 * @author Douglas Giordano
 */
@Entity
@Table(name = "commit_comment")
@Data
public class CommitComment implements Serializable, EntityBD {

    @Id
    private long id;
    private String sha;

    @ManyToOne()
    private Commit commit;
    private String body;
    private String bodyHtml;
    private String bodyText;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdAt;
    private String diffHunk;
    private int line;
    private int originalPosition;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedAt;
    @Lob
    @Column(length = 10000)
    private String path;
    private String url;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private br.edu.ufsm.model.User user;
    @Embedded
    private Sentimento sentimento;

    public CommitComment() {
    }

    public CommitComment(org.eclipse.egit.github.core.CommitComment comment, Commit commit) {
        this.commit = commit;
        this.body = comment.getBody();
        this.bodyHtml = comment.getBodyHtml();
        this.bodyText = comment.getBodyText();
        this.id = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.diffHunk = comment.getDiffHunk();
        this.line = comment.getLine();
        this.originalPosition = comment.getOriginalPosition();
        this.updatedAt = comment.getUpdatedAt();
        this.path = comment.getPath();
        this.user = new br.edu.ufsm.model.User(comment.getUser());
        this.url = comment.getUrl();
        this.commit = commit;
    }

    @Override
    public Object getPk() {
        return this.id;
    }

}
