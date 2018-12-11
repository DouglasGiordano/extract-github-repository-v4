/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.requestpostgraphql.entity;

/**
 *
 * @author Douglas Giordano
 * @since 18/11/2018
 */
public interface InterfaceStatus {
    public void setError(String error);
    public void setErrorResponse(String error);
    public void setErrorQuery(String error);
    public String getError();
    public String getErrorResponse();
    public String getErrorQuery();
    public InterfaceStatus save();
}
