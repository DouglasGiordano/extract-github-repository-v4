/*
 */
package br.edu.ufsm.requestpostgraphql.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lombok.Data;

/**
 *
 * @author Douglas Giordano
 */
@Entity
@Data
public class Status implements Serializable{

    @Id
    @GeneratedValue
    long id;
    private String endCursorPull;
    private String endCursorIssue;
    private boolean completePull;
    private boolean completeIssue;
    private String error;
    @Column(length = 10000)
    @Lob
    private String errorResponse;
    @Column(length = 10000)
    @Lob
    private String errorQuery;
    private String owner;
    private String name;

    public Status() {
    }

    public Status(String owner, String name) {
        this.owner = owner;
        this.name = name;
        this.completeIssue = false;
        this.completePull = false;
    }
}
