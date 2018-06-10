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
public enum TypeEventIssue {
    TYPE_CLOSED("closed"),
    TYPE_REOPENED("reopened"),
    TYPE_SUBSCRIBED("subscribed"),
    TYPE_MERGED("merged"),
    TYPE_REFERENCED("referenced"),
    TYPE_MENTIONED("mentioned"),
    TYPE_ASSIGNED("assigned"),
    TYPE_UNASSIGNED("unassigned"),
    TYPE_LABELED("labeled"),
    TYPE_UNLABELED("unlabeled"),
    TYPE_MILESTONED("milestoned"),
    TYPE_DEMILESTONED("demilestoned"),
    TYPE_RENAMED("renamed"),
    TYPE_LOCKED("locked"),
    TYPE_UNLOCKED("unlocked"),
    TYPE_HEAD_REF_DELETED("head_ref_deleted"),
    TYPE_HEAD_REF_RESTORED("head_ref_restored");

    private final String descricao;

    TypeEventIssue(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public static TypeEventIssue getType(String type) {
        switch (type) {
            case "closed":
                return TYPE_CLOSED;
            case "reopened":
                return TYPE_REOPENED;
            case "subscribed":
                return TYPE_SUBSCRIBED;
            case "merged":
                return TYPE_MERGED;
            case "referenced":
                return TYPE_REFERENCED;
            case "mentioned":
                return TYPE_MENTIONED;
            case "assigned":
                return TYPE_ASSIGNED;
            case "unassigned":
                return TYPE_UNASSIGNED;
            case "labeled":
                return TYPE_LABELED;
            case "unlabeled":
                return TYPE_UNLABELED;
            case "milestoned":
                return TYPE_MILESTONED;
            case "demilestoned":
                return TYPE_DEMILESTONED;
            case "renamed":
                return TYPE_RENAMED;
            case "locked":
                return TYPE_LOCKED;
            case "unlocked":
                return TYPE_UNLOCKED;
            case "head_ref_deleted":
                return TYPE_HEAD_REF_DELETED;
            case "head_ref_restored":
                return TYPE_HEAD_REF_RESTORED;
        }
        return null;
    }
}
