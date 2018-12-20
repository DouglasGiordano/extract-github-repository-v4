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
public class PullComment implements Serializable {

    @Id
    private String id;
    @Column(length = 10000)
    @Lob
    private String bodyHTML;
    private String createdAt;
    private String updatedAt;
    private String author;
    private String owner;
    private String url;
    private String name;
    private String pull;



    @Override
    public String toString() {
        return "Id: " + this.id + " url:" + this.url;
    }
}
