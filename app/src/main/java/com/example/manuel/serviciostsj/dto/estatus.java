package com.example.manuel.serviciostsj.dto;

/**
 * Created by Manuel on 12/07/2017.
 */

public class estatus {
    private int ID;
    private String ESTATUS;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getESTATUS() {
        return ESTATUS;
    }

    public void setESTATUS(String ESTATUS) {
        this.ESTATUS = ESTATUS;
    }

    public estatus(int id, String estatus){
        setID(id);
        setESTATUS(estatus);
    }

    public estatus(){

    }

}
