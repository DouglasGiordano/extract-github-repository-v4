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
 * @author Douglas Giordano
 */
@Entity
@Table(name = "pull_request")
public class PullRequest implements Serializable, EntityBD {

    @Id
    private long id;
    private boolean mergeable;
    private boolean merged;
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date mergedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private int additions;
    private int changedFiles;
    private int comments;
    private int commits;
    private int deletions;
    private int number;
    @OneToOne(cascade = CascadeType.ALL)
    private Milestone milestone;
    @ManyToOne(cascade = CascadeType.ALL)
    private PullRequestMarker base;
    @ManyToOne(cascade = CascadeType.ALL)
    private PullRequestMarker head;
    @Lob
    @Column(length = 10000)
    private String body;
    @Lob
    @Column(length = 10000)
    private String bodyHtml;
    @Lob
    @Column(length = 10000)
    private String bodyText;
    private String diffUrl;
    private String htmlUrl;
    private String issueUrl;
    private String patchUrl;
    private String state;
    @Column(length = 500)
    private String title;
    private String url;
    @ManyToOne(cascade = CascadeType.ALL)
    private User assignee;
    @ManyToOne(cascade = CascadeType.ALL)
    private User mergedBy;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @ManyToOne(cascade = CascadeType.ALL)
    private Project project;


    @Embedded
    protected Sentimento sentimento;
    public PullRequest(org.eclipse.egit.github.core.PullRequest pull, Project project) {
        if(pull==null){
            return;
        }
        this.project = project;
        this.id = pull.getId();
        this.mergeable = pull.isMergeable();
        this.merged = pull.isMerged();
        this.closedAt = pull.getClosedAt();
        this.mergedAt = pull.getMergedAt();
        this.updatedAt = pull.getUpdatedAt();
        this.createdAt = pull.getCreatedAt();
        this.additions = pull.getAdditions();
        this.changedFiles = pull.getChangedFiles();
        this.comments = pull.getComments();
        this.commits = pull.getCommits();
        this.deletions = pull.getDeletions();
        this.number = pull.getNumber();
        if(pull.getMilestone() != null && pull.getMilestone().getUrl() != null){
            this.milestone = new Milestone(pull.getMilestone());
        }
        if(pull.getBase() != null && pull.getBase().getSha() != null){
            this.base = new PullRequestMarker(pull.getBase());
        }
        if(pull.getHead() != null && pull.getHead().getSha() != null){
            this.head = new PullRequestMarker(pull.getHead());
        }

        this.body = pull.getBody();
        this.bodyHtml = pull.getBodyHtml();
        this.bodyText = pull.getBodyText();
        this.diffUrl = pull.getDiffUrl();
        this.htmlUrl = pull.getHtmlUrl();
        this.issueUrl = pull.getIssueUrl();
        this.patchUrl = pull.getPatchUrl();
        this.state = pull.getState();
        this.title = pull.getTitle();
        this.url = pull.getUrl();
        if(pull.getAssignee() != null && pull.getAssignee().getId() != 0){
            this.assignee = new User(pull.getAssignee());
        }
        if(pull.getAssignee() != null && pull.getAssignee().getId() != 0){
            this.assignee = new User(pull.getAssignee());
        }
        if(pull.getMergedBy() != null && pull.getMergedBy().getId() != 0){
            this.mergedBy = new User(pull.getMergedBy());
        }
        if(pull.getUser() != null && pull.getUser().getId() != 0){
            this.user = new User(pull.getUser());
        }
    }

    public PullRequest() {

    }

    /**
     * @return the mergeable
     */
    public boolean isMergeable() {
        return mergeable;
    }

    /**
     * @param mergeable the mergeable to set
     */
    public void setMergeable(boolean mergeable) {
        this.mergeable = mergeable;
    }

    /**
     * @return the merged
     */
    public boolean isMerged() {
        return merged;
    }

    /**
     * @param merged the merged to set
     */
    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    /**
     * @return the closedAt
     */
    public Date getClosedAt() {
        return closedAt;
    }

    /**
     * @param closedAt the closedAt to set
     */
    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    /**
     * @return the mergedAt
     */
    public Date getMergedAt() {
        return mergedAt;
    }

    /**
     * @param mergedAt the mergedAt to set
     */
    public void setMergedAt(Date mergedAt) {
        this.mergedAt = mergedAt;
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
     * @return the additions
     */
    public int getAdditions() {
        return additions;
    }

    /**
     * @param additions the additions to set
     */
    public void setAdditions(int additions) {
        this.additions = additions;
    }

    /**
     * @return the changedFiles
     */
    public int getChangedFiles() {
        return changedFiles;
    }

    /**
     * @param changedFiles the changedFiles to set
     */
    public void setChangedFiles(int changedFiles) {
        this.changedFiles = changedFiles;
    }

    /**
     * @return the comments
     */
    public int getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(int comments) {
        this.comments = comments;
    }

    /**
     * @return the commits
     */
    public int getCommits() {
        return commits;
    }

    /**
     * @param commits the commits to set
     */
    public void setCommits(int commits) {
        this.commits = commits;
    }

    /**
     * @return the deletions
     */
    public int getDeletions() {
        return deletions;
    }

    /**
     * @param deletions the deletions to set
     */
    public void setDeletions(int deletions) {
        this.deletions = deletions;
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
     * @return the milestone
     */
    public Milestone getMilestone() {
        return milestone;
    }

    /**
     * @param milestone the milestone to set
     */
    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    /**
     * @return the base
     */
    public PullRequestMarker getBase() {
        return base;
    }

    /**
     * @param base the base to set
     */
    public void setBase(PullRequestMarker base) {
        this.base = base;
    }

    /**
     * @return the head
     */
    public PullRequestMarker getHead() {
        return head;
    }

    /**
     * @param head the head to set
     */
    public void setHead(PullRequestMarker head) {
        this.head = head;
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
     * @return the diffUrl
     */
    public String getDiffUrl() {
        return diffUrl;
    }

    /**
     * @param diffUrl the diffUrl to set
     */
    public void setDiffUrl(String diffUrl) {
        this.diffUrl = diffUrl;
    }

    /**
     * @return the htmlUrl
     */
    public String getHtmlUrl() {
        return htmlUrl;
    }

    /**
     * @param htmlUrl the htmlUrl to set
     */
    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    /**
     * @return the issueUrl
     */
    public String getIssueUrl() {
        return issueUrl;
    }

    /**
     * @param issueUrl the issueUrl to set
     */
    public void setIssueUrl(String issueUrl) {
        this.issueUrl = issueUrl;
    }

    /**
     * @return the patchUrl
     */
    public String getPatchUrl() {
        return patchUrl;
    }

    /**
     * @param patchUrl the patchUrl to set
     */
    public void setPatchUrl(String patchUrl) {
        this.patchUrl = patchUrl;
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
     * @return the assignee
     */
    public User getAssignee() {
        return assignee;
    }

    /**
     * @param assignee the assignee to set
     */
    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    /**
     * @return the mergedBy
     */
    public User getMergedBy() {
        return mergedBy;
    }

    /**
     * @param mergedBy the mergedBy to set
     */
    public void setMergedBy(User mergedBy) {
        this.mergedBy = mergedBy;
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


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Sentimento getSentimento() {
        return sentimento;
    }

    public void setSentimento(Sentimento sentimento) {
        this.sentimento = sentimento;
    }

    @Override
    public Object getPk() {
        return this.id;
    }
}
