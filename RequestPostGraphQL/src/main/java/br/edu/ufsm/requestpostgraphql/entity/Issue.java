/*
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
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Douglas Giordano
 */
@Data
@Entity
public class Issue implements Serializable {

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

    @Override
    public String toString() {
        return "Id: " + this.id + " number:" + this.number;
    }
}
