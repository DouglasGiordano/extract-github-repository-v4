/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import br.edu.ufsm.persistence.EntityBD;
import com.vdurmont.emoji.EmojiParser;

import javax.persistence.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Douglas Giordano
 */
@Entity
@Table(name = "commit")
public class Commit implements Serializable, EntityBD {

    @Id
    private String sha;
    private int commentCount;
    @Lob
    @Column(length = 10000)
    private String message;
    @Embedded
    private CommitStats stats;

    @AttributeOverrides({
        @AttributeOverride(name = "date", column = @Column(name = "date_author_commit"))
        ,
        @AttributeOverride(name = "email", column = @Column(name = "email_author_commit"))
        ,
        @AttributeOverride(name = "name", column = @Column(name = "name_author_commit"))
    })
    @Embedded
    private CommitUser authorCommit;

    @AttributeOverrides({
        @AttributeOverride(name = "date", column = @Column(name = "date_commiter_commit"))
        ,
        @AttributeOverride(name = "email", column = @Column(name = "email_commiter_commit"))
        ,
        @AttributeOverride(name = "name", column = @Column(name = "name_commiter_commit"))
    })
    @Embedded
    private CommitUser commiterCommit;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "commit", cascade = CascadeType.ALL)
    private List<CommitFile> files;
    private String url;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User author;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User committer;
    @ManyToOne(cascade = CascadeType.ALL)
    private Project project;
    @Embedded
    private Sentimento sentimento;

    public Commit() {
    }

    public Commit(org.eclipse.egit.github.core.RepositoryCommit commit, Project project) {
        this.project = project;
        this.commentCount = commit.getCommit().getCommentCount();
        this.sha = commit.getSha();
        this.setMessage(commit.getCommit().getMessage());
        this.stats = new CommitStats(commit.getStats());
        if (commit.getFiles() != null) {
            files = new ArrayList<>();
            for (org.eclipse.egit.github.core.CommitFile file : commit.getFiles()) {
                files.add(new CommitFile(file, this.sha, this));
                file.setPatch(null);
            }
        }

        this.url = commit.getUrl();
        if (commit.getAuthor() != null) {
            this.author = new User(commit.getAuthor());
        }
        if (commit.getCommitter() != null) {
            this.committer = new User(commit.getCommitter());
        }
        if (commit.getCommit().getCommitter() != null) {
            commiterCommit = new CommitUser(commit.getCommit().getCommitter());
        }
        if (commit.getCommit().getAuthor() != null) {
            authorCommit = new CommitUser(commit.getCommit().getAuthor());
        }
        this.sentimento = new Sentimento();
    }

    /**
     * @return the commentCount
     */
    public int getCommentCount() {
        return commentCount;
    }

    /**
     * @param commentCount the commentCount to set
     */
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        try {
            String stringUtf8 = new String(message.getBytes("UTF-8"));
            byte[] ptext = stringUtf8.getBytes(ISO_8859_1);
            String value = new String(ptext, UTF_8);
            String result = EmojiParser.removeAllEmojis(value);
            this.message = result;

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Commit.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the stats
     */
    public CommitStats getStats() {
        return stats;
    }

    /**
     * @param stats the stats to set
     */
    public void setStats(CommitStats stats) {
        this.stats = stats;
    }

    /**
     * @return the files
     */
    public List<CommitFile> getFiles() {
        return files;
    }

    /**
     * @return the id
     */
    public Object getPk() {
        return sha;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(List<CommitFile> files) {
        this.files = files;
    }

    /**
     * @return the sha
     */
    public String getSha() {
        return sha;
    }

    /**
     * @param sha the sha to set
     */
    public void setSha(String sha) {
        this.sha = sha;
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
     * @return the author
     */
    public User getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * @return the committer
     */
    public User getCommitter() {
        return committer;
    }

    /**
     * @param committer the committer to set
     */
    public void setCommitter(User committer) {
        this.committer = committer;
    }

    private String convertToUTF8(String str) {
        Charset UTF_8 = Charset.forName("UTF-8");
        byte[] byteArray = str.getBytes(UTF_8);
        return new String(byteArray, UTF_8);
    }

    /**
     * @return the authorCommit
     */
    public CommitUser getAuthorCommit() {
        return authorCommit;
    }

    /**
     * @param authorCommit the authorCommit to set
     */
    public void setAuthorCommit(CommitUser authorCommit) {
        this.authorCommit = authorCommit;
    }

    /**
     * @return the commiterCommit
     */
    public CommitUser getCommiterCommit() {
        return commiterCommit;
    }

    /**
     * @param commiterCommit the commiterCommit to set
     */
    public void setCommiterCommit(CommitUser commiterCommit) {
        this.commiterCommit = commiterCommit;
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
