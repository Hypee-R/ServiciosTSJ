package com.example.manuel.serviciostsj.dto;

/**
 * Created by Manuel on 12/07/2017.
 */

public class tipos_parentesco {
    private int ID;
    private String DESCRIPCION;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }

    public tipos_parentesco(int id, String descripcion){
        setID(id);
        setDESCRIPCION(descripcion);
    }

    public tipos_parentesco(){

    }
}
