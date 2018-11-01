/*
 */
package br.edu.ufsm.requestpostgraphql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Douglas Giordano
 */
@Data
public class Repository {

    private String name;
    private String owner;
    private List<Complex> fields;

    public Repository(String owner, String name) {
        this.name = name;
        this.owner = owner;
    }
    
    public void setFields(Complex[] fields){
        this.fields = new ArrayList<>(Arrays.asList(fields));
    }
    
    public void setFields(List fields){
        this.fields = fields;
    }

    @Override
    public String toString() {
        StringBuilder query = new StringBuilder();
        query.append("query {");
        query.append("repository(owner: \\\"").append(owner).append("\\\", name: \\\"").append(name).append("\\\") { ");
        for(Complex c: fields){
            query.append(c.toString());
        }
        query.append("}");
        query.append("rateLimit {     limit     cost     remaining     resetAt   }");
        query.append("}");

        return query.toString();
    }
}
