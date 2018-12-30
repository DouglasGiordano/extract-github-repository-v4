/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.requestpostgraphql.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Douglas Giordano
 * @since 18/11/2018
 */
@Data
@Entity
public class Branch implements Serializable {

    @Id
    private String id;
    private String name;
    private String owner;
    private String repository;
    private boolean defaultRepository;
    boolean complete;
    @Column(length = 10000)
    @Lob
    private String errorResponse;
    @Column(length = 10000)
    @Lob
    private String errorQuery;
    private String error;
    private String endCursorCommit;
}
