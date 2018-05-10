/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import br.edu.ufsm.persistence.EntityBD;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;

/**
 *
 * @author Douglas Giordano
 */
@Entity
@Table(name = "issue_comment")
public class IssueComment implements Serializable, EntityBD {

    @Id
    private long id;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Temporal(TemporalType.DATE)
    private Date updatedAt;
    @Lob
    @Column(length = 10000)
    private String body;
    @Lob
    @Column(length = 10000)
    private String bodyHtml;
    @Lob
    @Column(length = 10000)
    private String bodyText;
    private String url;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @ManyToOne(cascade = CascadeType.ALL)
    private Issue issue;
    @Embedded
    private Sentimento sentimento;

    public IssueComment(org.eclipse.egit.github.core.Comment comment, Issue issue) {
        this.id = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.body = comment.getBody();
        this.bodyHtml = convertToUTF8(comment.getBodyHtml());
        this.bodyText = convertToUTF8(comment.getBodyText());
        this.url = comment.getUrl();
        this.user = new User(comment.getUser());
        this.issue = issue;
        this.sentimento = new Sentimento();
    }
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private String convertToUTF8(String str) {
        byte[] byteArray = str.getBytes(UTF_8);
        return new String(byteArray, UTF_8);
    }

    private String convertToUTF8(int hexString) {
        char[] emojiCharArray = Character.toChars(hexString);
        return new String(emojiCharArray);
    }
    public IssueComment() {

    }

    /**
     * @return the pk
     */
    @Override
    public Object getPk() {
        return this.id;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
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
     * @return the updatedAt
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the bodyHtml
     */
    public String getBodyHtml() {
        return bodyHtml;
    }

    /**
     * @param bodyHtml the bodyHtml to set
     */
    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    /**
     * @return the bodyText
     */
    public String getBodyText() {
        return bodyText;
    }

    /**
     * @param bodyText the bodyText to set
     */
    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
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
     * @return the issue
     */
    public Issue getIssue() {
        return issue;
    }

    /**
     * @param issue the issue to set
     */
    public void setIssue(Issue issue) {
        this.issue = issue;
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
