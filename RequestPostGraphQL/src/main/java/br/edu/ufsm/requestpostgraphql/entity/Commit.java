/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.requestpostgraphql.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;

/**
 *
 * @author Douglas Giordano
 * @since 18/11/2018
 */
@Data
@Entity
public class Commit {

    @Id
    private String id;
    private int additions;
    private int deletions;
    private int changedFiles;
    private boolean commiterNotFound;
    private boolean authorNotFound;
    private String author;
    private String committer;
    private boolean authoredByCommitter;
    private Date authoredDate;
    private Date committedDate;
    private String commitUrl;
    @Column(length = 1000)
    @Lob
    private String message;
    private String url;
    private String name;
    private String owner;
}
