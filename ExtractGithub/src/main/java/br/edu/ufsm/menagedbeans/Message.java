/*
 * Mensagem do sistema
 */
package br.edu.ufsm.menagedbeans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Douglas Giordano
 * @since 07/02/2017
 */
public class Message {

    public static void printMessageInfo(String message) {
        FacesContext.getCurrentInstance().addMessage("form", new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem: ", message));
    }

    public static void printMessageInfo(String message, String nameForm) {
        FacesContext.getCurrentInstance().addMessage(nameForm, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem: ", message));
    }

    public static void printMessageInfo(String title, String message, String nameForm) {
        FacesContext.getCurrentInstance().addMessage(nameForm, new FacesMessage(FacesMessage.SEVERITY_INFO, title, message));
    }

    public static void printMessageError(String message, String nameForm) {
        FacesContext.getCurrentInstance().addMessage(nameForm, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema: ", message));
    }

    public static void printMessageError(String title, String message, String nameForm) {
        FacesContext.getCurrentInstance().addMessage(nameForm, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, message));
    }

    public static void printMessageError(String message) {
        FacesContext.getCurrentInstance().addMessage("form", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema: ", message));
    }

    public static void printMessageWarning(String message, String nameForm) {
        FacesContext.getCurrentInstance().addMessage(nameForm, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso: ", message));
    }

    public static void printMessageWarning(String title, String message, String nameForm) {
        FacesContext.getCurrentInstance().addMessage(nameForm, new FacesMessage(FacesMessage.SEVERITY_WARN, title, message));
    }

    public static void printMessageWarning(String message) {
        FacesContext.getCurrentInstance().addMessage("form", new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso: ", message));
    }

    public static void printWarning(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso: ", message));
    }
}
