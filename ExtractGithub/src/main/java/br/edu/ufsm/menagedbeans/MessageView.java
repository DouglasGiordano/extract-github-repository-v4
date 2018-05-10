/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.menagedbeans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author douglas
 */
public class MessageView {

    public static void showMessage(String title, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(title, message));
    }

    public static void showMessageDanger(String title, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, message));
    }

    public static void showMessageWarning(String title, String message) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, title, message));
    }
}
