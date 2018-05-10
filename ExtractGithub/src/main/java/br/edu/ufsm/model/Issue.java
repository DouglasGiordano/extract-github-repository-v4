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
import java.util.List;

/**
 *
 * @author Douglas Giordano
 */
@Entity
@Table(name = "issue")
public class Issue implements Serializable, EntityBD {
    @Id
    private long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    private int comments;
    private int number;
    @OneToOne(cascade = CascadeType.ALL)
    private Milestone milestone;
    @ManyToOne(cascade = CascadeType.ALL)
    private PullRequest pullRequest;
    @Lob
    @Column(length = 10000)
    private String body;
    @Lob
    @Column(length = 10000)
    private String bodyHtml;
    @Lob
    @Column(length = 10000)
    private String bodyText;
    private String htmlUrl;
    private String state;
    @Column(length = 500)
    private String title;
    private String url;
    @ManyToOne(cascade = CascadeType.ALL)
    private User assignee;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @Column(length = 10000)
    private String label;
    @ManyToOne(cascade = CascadeType.ALL)
    private User closedBy;
    @ManyToOne(cascade = CascadeType.ALL)
    private Project project;
    @Embedded
    protected Sentimento sentimento;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "issue", cascade = CascadeType.ALL)
    private List<IssueComment> issueComments;

    public Issue(org.eclipse.egit.github.core.Issue issue, Project project) {
        this.id = issue.getId();
        this.project = project;
        this.closedAt = issue.getClosedAt();
        this.createdAt = issue.getCreatedAt();
        this.updatedAt = issue.getUpdatedAt();
        this.comments = issue.getComments();
        this.number = issue.getNumber();
        if(issue.getMilestone() != null && issue.getMilestone().getNumber() != 0){
            this.milestone = new Milestone(issue.getMilestone());
        }
        if(issue.getPullRequest() != null && issue.getPullRequest().getId() != 0){
            this.pullRequest = new PullRequest(issue.getPullRequest(), project);
        }
        this.body = issue.getBody();
        this.bodyHtml = issue.getBodyHtml();
        this.bodyText = issue.getBodyText();
        this.htmlUrl = issue.getHtmlUrl();
        this.state = issue.getState();
        this.title = issue.getTitle();
        this.url = issue.getUrl();
        if(issue.getAssignee() != null && issue.getAssignee().getLogin() != null && !issue.getAssignee().getLogin().equals("")){
            this.assignee = new User(issue.getAssignee());
        }

        if(issue.getUser() != null && issue.getUser().getLogin() != null && !issue.getUser().getLogin().equals("")){
            this.user = new User(issue.getUser());
        }

        if (issue.getLabels() != null) {
            setLabel("");
            for (org.eclipse.egit.github.core.Label label : issue.getLabels()) {
                this.setLabel(this.getLabel() + label.getName());
                this.setLabel(this.getLabel() + " "+ label.getColor()+";");
            }
        }
        this.sentimento = new Sentimento();
    }

    public Issue() {

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
     * @return the pk
     */
    @Override
    public Object getPk() {
        return this.id;
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
     * @return the pullRequest
     */
    public PullRequest getPullRequest() {
        return pullRequest;
    }

    /**
     * @param pullRequest the pullRequest to set
     */
    public void setPullRequest(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
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
     * @return the closedBy
     */
    public User getClosedBy() {
        return closedBy;
    }

    /**
     * @param closedBy the closedBy to set
     */
    public void setClosedBy(User closedBy) {
        this.closedBy = closedBy;
    }

    /**
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the issueComments
     */
    public List<IssueComment> getIssueComments() {
        return issueComments;
    }

    /**
     * @param issueComments the issueComments to set
     */
    public void setIssueComments(List<IssueComment> issueComments) {
        this.issueComments = issueComments;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
