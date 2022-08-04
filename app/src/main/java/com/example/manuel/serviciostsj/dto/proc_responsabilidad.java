package com.example.manuel.serviciostsj.dto;

/**
 * Created by Manuel on 06/07/2017.
 */

public class proc_responsabilidad {
    private int ID;
    private int ID_TRABAJADOR;
    private String NOMBRE_PROMOVENTE;
    private String FECHA;
    private String NUMERO_EXPEDIENTE;
    private String SENTIDO_RESOLUCION;
    private String SANCION;
    private String TERMINOS_SANCION;
    private String OBSERVACIONES;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID_TRABAJADOR() {
        return ID_TRABAJADOR;
    }

    public void setID_TRABAJADOR(int ID_TRABAJADOR) {
        this.ID_TRABAJADOR = ID_TRABAJADOR;
    }

    public String getNOMBRE_PROMOVENTE() {
        return NOMBRE_PROMOVENTE;
    }

    public void setNOMBRE_PROMOVENTE(String NOMBRE_PROMOVENTE) {
        this.NOMBRE_PROMOVENTE = NOMBRE_PROMOVENTE;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }

    public String getNUMERO_EXPEDIENTE() {
        return NUMERO_EXPEDIENTE;
    }

    public void setNUMERO_EXPEDIENTE(String NUMERO_EXPEDIENTE) {
        this.NUMERO_EXPEDIENTE = NUMERO_EXPEDIENTE;
    }

    public String getSENTIDO_RESOLUCION() {
        return SENTIDO_RESOLUCION;
    }

    public void setSENTIDO_RESOLUCION(String SENTIDO_RESOLUCION) {
        this.SENTIDO_RESOLUCION = SENTIDO_RESOLUCION;
    }

    public String getSANCION() {
        return SANCION;
    }

    public void setSANCION(String SANCION) {
        this.SANCION = SANCION;
    }

    public String getTERMINOS_SANCION() {
        return TERMINOS_SANCION;
    }

    public void setTERMINOS_SANCION(String TERMINOS_SANCION) {
        this.TERMINOS_SANCION = TERMINOS_SANCION;
    }

    public String getOBSERVACIONES() {
        return OBSERVACIONES;
    }

    public void setOBSERVACIONES(String OBSERVACIONES) {
        this.OBSERVACIONES = OBSERVACIONES;
    }

    public  proc_responsabilidad(int id, int id_trbjdr, String n_promovente, String fecha, String n_expediente, String s_resolucion,
                                 String sancion, String t_sancion, String observaciones){
        setID(id);
        setID_TRABAJADOR(id_trbjdr);
        setNOMBRE_PROMOVENTE(n_promovente);
        setFECHA(fecha);
        setNUMERO_EXPEDIENTE(n_expediente);
        setSENTIDO_RESOLUCION(s_resolucion);
        setSANCION(sancion);
        setTERMINOS_SANCION(t_sancion);
        setOBSERVACIONES(observaciones);

    }

    public proc_responsabilidad(){

    }

}
