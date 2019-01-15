/*
 */
package br.edu.ufsm.requestpostgraphql.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;

/**
 *
 * @author Douglas Giordano
 */
@Data
@Entity
public class PullRequest implements Serializable {

    @Id
    private String id;
    private String number;
    @Column(length = 1000)
    @Lob
    private String title;
    private String createdAt;
    private String updatedAt;
    private String author;
    private String owner;
    private String name;
    private String url;
    private int comments;
    @Lob
    private String bodyHTML;


    @Override
    public String toString() {
        return "Id: " + this.id + " number:" + this.number;
    }
   
}
