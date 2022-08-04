package com.example.manuel.serviciostsj.dto;

/**
 * Created by Manuel on 05/07/2017.
 */

public class historia_curricular {
    private int ID;
    private int ID_TRABAJADOR;
    private String CURSO;
    private String FECHA_INICIO;
    private String FECHA_FIN;
    private String DURACION;
    private String INSTITUCION_EXPIDE;
    private String LUGAR_EXPEDICION;
    private String QUIEN_AUTORIZA;
    private String CALIFICACION;
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

    public String getCURSO() {
        return CURSO;
    }

    public void setCURSO(String CURSO) {
        this.CURSO = CURSO;
    }

    public String getFECHA_INICIO() {
        return FECHA_INICIO;
    }

    public void setFECHA_INICIO(String FECHA_INICIO) {
        this.FECHA_INICIO = FECHA_INICIO;
    }

    public String getFECHA_FIN() {
        return FECHA_FIN;
    }

    public void setFECHA_FIN(String FECHA_FIN) {
        this.FECHA_FIN = FECHA_FIN;
    }

    public String getDURACION() {
        return DURACION;
    }

    public void setDURACION(String DURACION) {
        this.DURACION = DURACION;
    }

    public String getINSTITUCION_EXPIDE() {
        return INSTITUCION_EXPIDE;
    }

    public void setINSTITUCION_EXPIDE(String INSTITUCION_EXPIDE) {
        this.INSTITUCION_EXPIDE = INSTITUCION_EXPIDE;
    }

    public String getLUGAR_EXPEDICION() {
        return LUGAR_EXPEDICION;
    }

    public void setLUGAR_EXPEDICION(String LUGAR_EXPEDICION) {
        this.LUGAR_EXPEDICION = LUGAR_EXPEDICION;
    }

    public String getQUIEN_AUTORIZA() {
        return QUIEN_AUTORIZA;
    }

    public void setQUIEN_AUTORIZA(String QUIEN_AUTORIZA) {
        this.QUIEN_AUTORIZA = QUIEN_AUTORIZA;
    }

    public String getCALIFICACION() {
        return CALIFICACION;
    }

    public void setCALIFICACION(String CALIFICACION) {
        this.CALIFICACION = CALIFICACION;
    }

    public String getOBSERVACIONES() {
        return OBSERVACIONES;
    }

    public void setOBSERVACIONES(String OBSERVACIONES) {
        this.OBSERVACIONES = OBSERVACIONES;
    }

    public historia_curricular(int id, int id_trbjdr, String curso, String f_inicio, String f_fin, String duracion,
                               String inst_exp, String l_expedicion, String q_autoriza, String calificacion, String observaciones){
        setID(id);
        setID_TRABAJADOR(id_trbjdr);
        setCURSO(curso);
        setFECHA_INICIO(f_inicio);
        setFECHA_FIN(f_fin);
        setDURACION(duracion);
        setINSTITUCION_EXPIDE(inst_exp);
        setLUGAR_EXPEDICION(l_expedicion);
        setQUIEN_AUTORIZA(q_autoriza);
        setCALIFICACION(calificacion);
        setOBSERVACIONES(observaciones);

    }

    public historia_curricular(){

    }

}
