package com.example.manuel.serviciostsj.dto;

/**
 * Created by Manuel on 13/07/2017.
 */

public class tipos_concepto {
    private int ID;
    private String CONCEPTO;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCONCEPTO() {
        return CONCEPTO;
    }

    public void setCONCEPTO(String CONCEPTO) {
        this.CONCEPTO = CONCEPTO;
    }

    public tipos_concepto(int id, String concepto){
        setID(id);
        setCONCEPTO(concepto);
    }

    public tipos_concepto(){

    }
}
