/*
 */
package br.edu.ufsm.model.simple;

import br.edu.ufsm.model.Sentimento;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import lombok.Data;

/**
 *
 * @author Douglas Giordano
 */
@Entity
@Data
public class SimpleComment implements Serializable {

    @Id
    private int id;
    private String user;
    private String project;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date create;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date update;
    private String url;
    @Embedded
    private Sentimento sentimento;
    private String text;
    private String origem;
}
