/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import br.edu.ufsm.persistence.EntityBD;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Dougl
 */
@Entity
@Table(name = "commit_file")
public class CommitFile implements Serializable, EntityBD {

    @Id
    @Column(length = 255)
    private String id;
    private String sha;
    private int additions;
    private int changes;
    private int deletions;
    private String blobUrl;
    private String filename;
    @Lob
    @Column(length = 10000)
    private String patch;
    @Column(length = 500)
    private String rawUrl;
    private String status;
    @ManyToOne(cascade = CascadeType.ALL)
    private Commit commit;
    
    public CommitFile() {
    }

    public CommitFile(org.eclipse.egit.github.core.CommitFile commitFile, String shaCommit, Commit commit) {
        this.commit = commit;
        this.additions = commitFile.getAdditions();
        this.changes = commitFile.getChanges();
        this.deletions = commitFile.getDeletions();
        this.blobUrl = commitFile.getBlobUrl();
        this.filename = commitFile.getFilename();
        this.patch = commitFile.getPatch();
        this.rawUrl = commitFile.getRawUrl();
        this.sha = commitFile.getSha();
        this.status = commitFile.getStatus();
        this.id = this.status+this.changes+this.deletions+this.additions+ shaCommit + this.filename+ this.sha;
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
     * @return the changes
     */
    public int getChanges() {
        return changes;
    }

    /**
     * @param changes the changes to set
     */
    public void setChanges(int changes) {
        this.changes = changes;
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
     * @return the blobUrl
     */
    public String getBlobUrl() {
        return blobUrl;
    }

    /**
     * @param blobUrl the blobUrl to set
     */
    public void setBlobUrl(String blobUrl) {
        this.blobUrl = blobUrl;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the patch
     */
    public String getPatch() {
        return patch;
    }

    /**
     * @param patch the patch to set
     */
    public void setPatch(String patch) {
        this.patch = patch;
    }

    /**
     * @return the rawUrl
     */
    public String getRawUrl() {
        return rawUrl;
    }

    /**
     * @param rawUrl the rawUrl to set
     */
    public void setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
    }

    /**
     * @return the sha
     */
    public String getSha() {
        return sha;
    }

    /**
     * @return the sha
     */
    public Object getPk() {
        return this.rawUrl;
    }

    /**
     * @param sha the sha to set
     */
    public void setSha(String sha) {
        this.sha = sha;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
     * @return the commit
     */
    public Commit getCommit() {
        return commit;
    }

    /**
     * @param commit the commit to set
     */
    public void setCommit(Commit commit) {
        this.commit = commit;
    }
}
