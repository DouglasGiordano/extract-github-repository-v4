/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import br.edu.ufsm.persistence.EntityBD;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Douglas Giordano
 */
@Entity
@Table(name = "user")
public class User implements Serializable, EntityBD {

    @Id
    private int id;
    private boolean hireable;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdAt;
    private int collaborators;
    private int diskUsage;
    private int followers;
    private int following;
    private int ownedPrivateRepos;
    private int privateGists;
    private int publicGists;
    private int publicRepos;
    private int totalPrivateRepos;
    private String avatarUrl;
    private String blog;
    private String company;
    private String email;
    private String gravatarId;
    private String htmlUrl;
    private String location;
    private String login;
    private String name;
    private String type;
    private String url;

    public User() {
    }

    public User(org.eclipse.egit.github.core.User user) {
        if(user == null){
            return;
        }
        this.hireable = user.isHireable();
        this.createdAt = user.getCreatedAt();
        this.collaborators = user.getCollaborators();
        this.diskUsage = user.getDiskUsage();
        this.followers = user.getFollowers();
        this.following = user.getFollowing();
        this.id = user.getId();
        this.ownedPrivateRepos = user.getOwnedPrivateRepos();
        this.privateGists = user.getPrivateGists();
        this.publicGists = user.getPublicGists();
        this.publicRepos = user.getPublicRepos();
        this.totalPrivateRepos = user.getTotalPrivateRepos();
        this.avatarUrl = user.getAvatarUrl();
        this.blog = user.getBlog();
        this.company = user.getCompany();
        this.email = user.getEmail();
        this.htmlUrl = user.getHtmlUrl();
        this.location = user.getLocation();
        this.login = user.getLogin();
        this.name = user.getName();
        this.type = user.getType();
        this.url = user.getUrl();
    }

    /**
     * @return the hireable
     */
    public boolean isHireable() {
        return hireable;
    }

    /**
     * @param hireable the hireable to set
     */
    public void setHireable(boolean hireable) {
        this.hireable = hireable;
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
     * @return the collaborators
     */
    public int getCollaborators() {
        return collaborators;
    }

    /**
     * @param collaborators the collaborators to set
     */
    public void setCollaborators(int collaborators) {
        this.collaborators = collaborators;
    }

    /**
     * @return the diskUsage
     */
    public int getDiskUsage() {
        return diskUsage;
    }

    /**
     * @param diskUsage the diskUsage to set
     */
    public void setDiskUsage(int diskUsage) {
        this.diskUsage = diskUsage;
    }

    /**
     * @return the followers
     */
    public int getFollowers() {
        return followers;
    }

    /**
     * @param followers the followers to set
     */
    public void setFollowers(int followers) {
        this.followers = followers;
    }

    /**
     * @return the following
     */
    public int getFollowing() {
        return following;
    }

    /**
     * @param following the following to set
     */
    public void setFollowing(int following) {
        this.following = following;
    }

    /**
     * @return the id
     */
    public int getId() {
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
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the ownedPrivateRepos
     */
    public int getOwnedPrivateRepos() {
        return ownedPrivateRepos;
    }

    /**
     * @param ownedPrivateRepos the ownedPrivateRepos to set
     */
    public void setOwnedPrivateRepos(int ownedPrivateRepos) {
        this.ownedPrivateRepos = ownedPrivateRepos;
    }

    /**
     * @return the privateGists
     */
    public int getPrivateGists() {
        return privateGists;
    }

    /**
     * @param privateGists the privateGists to set
     */
    public void setPrivateGists(int privateGists) {
        this.privateGists = privateGists;
    }

    /**
     * @return the publicGists
     */
    public int getPublicGists() {
        return publicGists;
    }

    /**
     * @param publicGists the publicGists to set
     */
    public void setPublicGists(int publicGists) {
        this.publicGists = publicGists;
    }

    /**
     * @return the publicRepos
     */
    public int getPublicRepos() {
        return publicRepos;
    }

    /**
     * @param publicRepos the publicRepos to set
     */
    public void setPublicRepos(int publicRepos) {
        this.publicRepos = publicRepos;
    }

    /**
     * @return the totalPrivateRepos
     */
    public int getTotalPrivateRepos() {
        return totalPrivateRepos;
    }

    /**
     * @param totalPrivateRepos the totalPrivateRepos to set
     */
    public void setTotalPrivateRepos(int totalPrivateRepos) {
        this.totalPrivateRepos = totalPrivateRepos;
    }

    /**
     * @return the avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * @param avatarUrl the avatarUrl to set
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * @return the blog
     */
    public String getBlog() {
        return blog;
    }

    /**
     * @param blog the blog to set
     */
    public void setBlog(String blog) {
        this.blog = blog;
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the gravatarId
     */
    public String getGravatarId() {
        return gravatarId;
    }

    /**
     * @param gravatarId the gravatarId to set
     */
    public void setGravatarId(String gravatarId) {
        this.gravatarId = gravatarId;
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
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
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


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.id;
        return hash;
    }
}
