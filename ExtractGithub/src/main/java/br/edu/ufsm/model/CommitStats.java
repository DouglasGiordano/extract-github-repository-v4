/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 *
 * @author Dougl
 */
@Embeddable
public class CommitStats implements Serializable {

    private int additions;
    private int deletions;
    private int total;

    public CommitStats() {
    }

    public CommitStats(org.eclipse.egit.github.core.CommitStats commitStats) {
        if(commitStats == null){
            return;
        }
        this.additions = commitStats.getAdditions();
        this.deletions = commitStats.getDeletions();
        this.total = commitStats.getTotal();
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
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }
}
