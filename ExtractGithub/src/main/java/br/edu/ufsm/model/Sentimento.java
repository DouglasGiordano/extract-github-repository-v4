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
 * @author douglas giordano
 */
@Embeddable
public class Sentimento implements Serializable {
    private int p;
    private int n;
    private int s;
    private int sc;

    /**
     * @return the p
     */
    public int getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(int p) {
        this.p = p;
    }

    /**
     * @return the n
     */
    public int getN() {
        return n;
    }

    /**
     * @param n the n to set
     */
    public void setN(int n) {
        this.n = n;
    }

    /**
     * @return the s
     */
    public int getS() {
        return s;
    }

    /**
     * @param s the s to set
     */
    public void setS(int s) {
        this.s = s;
    }

    /**
     * @return the sc
     */
    public int getSc() {
        return sc;
    }

    /**
     * @param sc the sc to set
     */
    public void setSc(int sc) {
        this.sc = sc;
    }
}
