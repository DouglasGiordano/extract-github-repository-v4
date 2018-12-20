package br.edu.ufsm.requestpostgraphql.service;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Douglas Giordano
 */
public class LoadManagement {

    private static LoadManagement INSTANCE;
    
    @Getter
    private int weight;
    
    @Getter
    @Setter
    private boolean heavyLoad;
    private int toBreak;
    @Getter
    private int toSucess;
    
    private LoadManagement() {

    }

    public static LoadManagement getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoadManagement();
        }
        return INSTANCE;
    }
    
    public void fixing(){
        this.toBreak = 0;
        this.heavyLoad = false;
    }
    
    public void breaking(){
        this.toBreak++;
    }
    
    public boolean isBreak(){
        return this.toBreak == 4;
    }
    
    public void init(){
        this.weight = 100;
        this.toSucess = 0;
        this.heavyLoad = false;
        this.toBreak = 0;
    }
    
    public void increase(){
        switch(this.weight){
            case 1:
                this.weight = 5;
                break;
            case 5:
                this.weight = 10;
                break;
            case 10:
                this.weight = 25;
                break;
            case 25:
                this.weight = 50;
                break;
            case 50:
                this.weight = 100;
                break;
        }
    }
    
    public void decrease(){
        this.heavyLoad = true;
        switch(this.weight){
            case 100:
                this.weight = 50;
                break;
            case 50:
                this.weight = 25;
                break;
            case 25:
                this.weight = 10;
                break;
            case 10:
                this.weight = 5;
                break;
            case 5:
                this.weight = 1;
                break;
        }
    }
    
    public void isSucess(){
        this.toSucess++;
        if(this.toSucess == 10){
            this.increase();
            this.toSucess = 0;
        }
    }
}
