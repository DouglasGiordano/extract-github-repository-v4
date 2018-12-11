/*
 */
package br.edu.ufsm.requestpostgraphql.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Douglas Giordano
 */
@Data
public class Simple {

    private String name;
    private List<String> fields;

    public Simple(String name, String[] fields) {
        this.name = name;
        this.fields = new ArrayList<>(Arrays.asList(fields));
    }

    @Override
    public String toString() {
        String query = name + " {";
        query = fields.stream().map((f) -> f + " ").reduce(query, String::concat);
        query += "}";
        return query;
    }
}
