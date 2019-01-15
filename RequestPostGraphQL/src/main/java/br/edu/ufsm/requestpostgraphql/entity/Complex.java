/*
 */
package br.edu.ufsm.requestpostgraphql.entity;

import br.edu.ufsm.requestpostgraphql.service.LoadManagement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Douglas Giordano
 */
@Data
public class Complex {

    private String name;
    private String nameNode;
    private List<String> fields;
    private List<Simple> simple;
    private List<Complex> complex;
    private String endCusor;

    public Complex(String name, String nameNode, String[] fields, Simple[] simple) {
        this.name = name;
        this.nameNode = nameNode;
        this.fields = new ArrayList<>(Arrays.asList(fields));
        this.simple = new ArrayList<>(Arrays.asList(simple));
    }

    public void setComplex(List<Complex> complex) {
        this.complex = complex;
    }

    public void setComplex(Complex[] complex) {
        this.complex = new ArrayList<>(Arrays.asList(complex));
    }

    @Override
    public String toString() {
        StringBuilder query = new StringBuilder();
        if (endCusor == null || endCusor.equalsIgnoreCase("")) {
            query.append(name).append("(first: ").append(LoadManagement.getInstance().getWeight()).append(") { ");
        } else {
            query.append(name).append("(first: ").append(LoadManagement.getInstance().getWeight()).append(", after: \\\"" + endCusor + "\\\"").append(") { ");
        }
        query.append("totalCount edges { ");
        query.append(nameNode).append(": node {  ");
        fields.forEach((f) -> {
            query.append(f).append(" ");
        });
        simple.forEach((s) -> {
            query.append(s.toString()).append(" ");
        });
        if (complex != null) {
            complex.forEach((s) -> {
                query.append(s.toString()).append(" ");
            });
        }

        query.append("}");
        query.append("}");
        query.append("pageInfo { endCursor hasNextPage hasPreviousPage }");
        query.append("}");

        return query.toString();
    }
}
