/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model.event;

/**
 *
 * @author Dougl
 */
public enum TypeEvent {
    TYPE_COMMIT_COMMENT("CommitCommentEvent"),
    TYPE_CREATE("CreateEvent"),
    TYPE_DELETE("DeleteEvent"),
    TYPE_DOWNLOAD("DownloadEvent"),
    TYPE_FOLLOW("FollowEvent"),
    TYPE_FORK("ForkEvent"),
    TYPE_FORK_APPLY("ForkApplyEvent"),
    TYPE_GIST("GistEvent"),
    TYPE_GOLLUM("GollumEvent"),
    TYPE_ISSUE_COMMENT("IssueCommentEvent"),
    TYPE_ISSUES("IssuesEvent"),
    TYPE_MEMBER("MemberEvent"),
    TYPE_PUBLIC("PublicEvent"),
    TYPE_PULL_REQUEST("PullRequestEvent"),
    TYPE_PULL_REQUEST_REVIEW_COMMENT("PullRequestReviewCommentEvent"),
    TYPE_PUSH("PushEvent"),
    TYPE_RELEASE("ReleaseEvent"),
    TYPE_TEAM_ADD("TeamAddEvent"),
    TYPE_WATCH("WatchEvent");

    private final String descricao;

    TypeEvent(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public static TypeEvent getType(String type) {
        switch (type) {
            case "CommitCommentEvent":
                return TYPE_COMMIT_COMMENT;
            case "CreateEvent":
                return TYPE_CREATE;
            case "DeleteEvent":
                return TYPE_DELETE;
            case "DownloadEvent":
                return TYPE_DOWNLOAD;
            case "FollowEvent":
                return TYPE_FOLLOW;
            case "ForkEvent":
                return TYPE_FORK;
            case "ForkApplyEvent":
                return TYPE_FORK_APPLY;
            case "GistEvent":
                return TYPE_GIST;
            case "GollumEvent":
                return TYPE_GOLLUM;
            case "IssueCommentEvent":
                return TYPE_ISSUE_COMMENT;
            case "IssuesEvent":
                return TYPE_ISSUES;
            case "MemberEvent":
                return TYPE_MEMBER;
            case "PublicEvent":
                return TYPE_PUBLIC;
            case "PullRequestEvent":
                return TYPE_PULL_REQUEST;
            case "PullRequestReviewCommentEvent":
                return TYPE_PULL_REQUEST_REVIEW_COMMENT;
            case "PushEvent":
                return TYPE_PUSH;
            case "ReleaseEvent":
                return TYPE_RELEASE;
            case "TeamAddEvent":
                return TYPE_TEAM_ADD;
            case "WatchEvent":
                return TYPE_WATCH;

        }
        return null;
    }
}
