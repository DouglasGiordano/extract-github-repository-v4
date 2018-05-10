/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import br.edu.ufsm.persistence.EntityBD;
import org.eclipse.egit.github.core.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Douglas Giordano
 */
@Entity
@Table(name = "project")
public class Project implements Serializable, EntityBD {
    @Id
    private long id;
    private boolean fork;
    private boolean hasDownloads;
    private boolean hasIssues;
    private boolean hasWiki;
    private boolean isPrivate;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Temporal(TemporalType.DATE)
    private Date pushedAt;
    @Temporal(TemporalType.DATE)
    private Date updatedAt;
    private int forks;
    private int openIssues;
    private int size;
    private int watchers;
    private Repository parent;
    private Repository source;
    private String cloneUrl;
    @Column(length = 10000)
    private String description;
    private String homepage;
    private String gitUrl;
    private String htmlUrl;
    private String language;
    private String masterBranch;
    private String mirrorUrl;
    private String name;
    private String sshUrl;
    private String svnUrl;
    private String url;
    @ManyToOne(cascade = CascadeType.ALL)
    private User owner;
    public Project() {
    }

    public Project(Repository repository) {
        if(repository == null){
            return;
        }
        this.fork = repository.isFork();
        this.hasDownloads = repository.isHasDownloads();
        this.hasIssues = repository.isHasIssues();
        this.hasWiki = repository.isHasWiki();
        this.isPrivate = repository.isPrivate();
        this.createdAt = repository.getCreatedAt();
        this.pushedAt = repository.getPushedAt();
        this.updatedAt = repository.getUpdatedAt();
        this.forks = repository.getForks();
        this.id = repository.getId();
        this.openIssues = repository.getOpenIssues();
        this.size = repository.getSize();
        this.watchers = repository.getWatchers();
        this.cloneUrl = repository.getCloneUrl();
        this.description = repository.getDescription();
        this.homepage = repository.getHomepage();
        this.gitUrl = repository.getUrl();
        this.htmlUrl = repository.getHtmlUrl();
        this.language = repository.getLanguage();
        this.mirrorUrl = repository.getMirrorUrl();
        this.name = repository.getName();
        this.sshUrl = repository.getSshUrl();
        this.svnUrl = repository.getSvnUrl();
        this.url = repository.getUrl();
        this.owner = new User(repository.getOwner());
    }

    /**
     * @return the fork
     */
    public boolean isFork() {
        return fork;
    }

    /**
     * @param fork the fork to set
     */
    public void setFork(boolean fork) {
        this.fork = fork;
    }

    /**
     * @return the hasDownloads
     */
    public boolean isHasDownloads() {
        return hasDownloads;
    }

    /**
     * @param hasDownloads the hasDownloads to set
     */
    public void setHasDownloads(boolean hasDownloads) {
        this.hasDownloads = hasDownloads;
    }

    /**
     * @return the hasIssues
     */
    public boolean isHasIssues() {
        return hasIssues;
    }

    /**
     * @param hasIssues the hasIssues to set
     */
    public void setHasIssues(boolean hasIssues) {
        this.hasIssues = hasIssues;
    }

    /**
     * @return the hasWiki
     */
    public boolean isHasWiki() {
        return hasWiki;
    }

    /**
     * @param hasWiki the hasWiki to set
     */
    public void setHasWiki(boolean hasWiki) {
        this.hasWiki = hasWiki;
    }

    /**
     * @return the isPrivate
     */
    public boolean isIsPrivate() {
        return isPrivate;
    }

    /**
     * @param isPrivate the isPrivate to set
     */
    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
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
     * @return the pushedAt
     */
    public Date getPushedAt() {
        return pushedAt;
    }

    /**
     * @param pushedAt the pushedAt to set
     */
    public void setPushedAt(Date pushedAt) {
        this.pushedAt = pushedAt;
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
     * @return the forks
     */
    public int getForks() {
        return forks;
    }

    /**
     * @param forks the forks to set
     */
    public void setForks(int forks) {
        this.forks = forks;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }
    
        /**
     * @return the id
     */
    @Override
    public Object getPk() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
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
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the watchers
     */
    public int getWatchers() {
        return watchers;
    }

    /**
     * @param watchers the watchers to set
     */
    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    /**
     * @return the parent
     */
    public Repository getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Repository parent) {
        this.parent = parent;
    }

    /**
     * @return the source
     */
    public Repository getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Repository source) {
        this.source = source;
    }

    /**
     * @return the cloneUrl
     */
    public String getCloneUrl() {
        return cloneUrl;
    }

    /**
     * @param cloneUrl the cloneUrl to set
     */
    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
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
     * @return the homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @param homepage the homepage to set
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * @return the gitUrl
     */
    public String getGitUrl() {
        return gitUrl;
    }

    /**
     * @param gitUrl the gitUrl to set
     */
    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
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
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the masterBranch
     */
    public String getMasterBranch() {
        return masterBranch;
    }

    /**
     * @param masterBranch the masterBranch to set
     */
    public void setMasterBranch(String masterBranch) {
        this.masterBranch = masterBranch;
    }

    /**
     * @return the mirrorUrl
     */
    public String getMirrorUrl() {
        return mirrorUrl;
    }

    /**
     * @param mirrorUrl the mirrorUrl to set
     */
    public void setMirrorUrl(String mirrorUrl) {
        this.mirrorUrl = mirrorUrl;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the sshUrl
     */
    public String getSshUrl() {
        return sshUrl;
    }

    /**
     * @param sshUrl the sshUrl to set
     */
    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    /**
     * @return the svnUrl
     */
    public String getSvnUrl() {
        return svnUrl;
    }

    /**
     * @param svnUrl the svnUrl to set
     */
    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
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
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Project other = (Project) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    
}
