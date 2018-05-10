package br.edu.ufsm.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "count_registers")
public class CountRegisters {
    @Id
    private Integer commit_count;
    private Integer commit_file_count;
    private Integer issue_count;
    private Integer issue_comment_count;
    private Integer mailinglist_count;
    private Integer user_count;
    private Integer pull_request_count;

    public Integer getCommit_count() {
        return commit_count;
    }

    public void setCommit_count(Integer commit_count) {
        this.commit_count = commit_count;
    }

    public Integer getCommit_file_count() {
        return commit_file_count;
    }

    public void setCommit_file_count(Integer commit_file_count) {
        this.commit_file_count = commit_file_count;
    }

    public Integer getIssue_count() {
        return issue_count;
    }

    public void setIssue_count(Integer issue_count) {
        this.issue_count = issue_count;
    }

    public Integer getIssue_comment_count() {
        return issue_comment_count;
    }

    public void setIssue_comment_count(Integer issue_comment_count) {
        this.issue_comment_count = issue_comment_count;
    }

    public Integer getMailinglist_count() {
        return mailinglist_count;
    }

    public void setMailinglist_count(Integer mailinglist_count) {
        this.mailinglist_count = mailinglist_count;
    }

    public Integer getUser_count() {
        return user_count;
    }

    public void setUser_count(Integer user_count) {
        this.user_count = user_count;
    }

    public Integer getPull_request_count() {
        return pull_request_count;
    }

    public void setPull_request_count(Integer pull_request_count) {
        this.pull_request_count = pull_request_count;
    }
}
