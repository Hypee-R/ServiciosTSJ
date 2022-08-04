package com.example.manuel.serviciostsj.dto;

/**
 * Created by Manuel on 25/06/2017.
 */

public class datos_laborales {
    private int ID;
    private int ID_TRABAJADOR;
    private String NOMBRAMIENTO;
    private String TIPO_CONTRATACION;
    private String LUGAR_ADSCRIPCION;
    private String FECHA_INGRESO;
    private String FECHA_REINGRESO;
    private String CAUSA_INASISTENCIA;
    private String OBSERVACIONES;
    private String FECHA_FIN_LICENCIA;
    private String FECHA_INICIO_LICENCIA;
    private String LICENCIA_MEDICA;
    private String NOMBRE;
    private String APELLIDO_PATERNO;
    private String APELLIDO_MATERNO;

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

    public String getNOMBRAMIENTO() {
        return NOMBRAMIENTO;
    }

    public void setNOMBRAMIENTO(String NOMBRAMIENTO) {
        this.NOMBRAMIENTO = NOMBRAMIENTO;
    }

    public String getTIPO_CONTRATACION() {
        return TIPO_CONTRATACION;
    }

    public void setTIPO_CONTRATACION(String TIPO_CONTRATACION) {
        this.TIPO_CONTRATACION = TIPO_CONTRATACION;
    }

    public String getLUGAR_ADSCRIPCION() {
        return LUGAR_ADSCRIPCION;
    }

    public void setLUGAR_ADSCRIPCION(String LUGAR_ADSCRIPCION) {
        this.LUGAR_ADSCRIPCION = LUGAR_ADSCRIPCION;
    }

    public String getFECHA_INGRESO() {
        return FECHA_INGRESO;
    }

    public void setFECHA_INGRESO(String FECHA_INGRESO) {
        this.FECHA_INGRESO = FECHA_INGRESO;
    }

    public String getFECHA_REINGRESO() {
        return FECHA_REINGRESO;
    }

    public void setFECHA_REINGRESO(String FECHA_REINGRESO) {
        this.FECHA_REINGRESO = FECHA_REINGRESO;
    }

    public String getCAUSA_INASISTENCIA() {
        return CAUSA_INASISTENCIA;
    }

    public void setCAUSA_INASISTENCIA(String CAUSA_INASISTENCIA) {
        this.CAUSA_INASISTENCIA = CAUSA_INASISTENCIA;
    }

    public String getOBSERVACIONES() {
        return OBSERVACIONES;
    }

    public void setOBSERVACIONES(String OBSERVACIONES) {
        this.OBSERVACIONES = OBSERVACIONES;
    }

    public String getFECHA_FIN_LICENCIA() {
        return FECHA_FIN_LICENCIA;
    }

    public void setFECHA_FIN_LICENCIA(String FECHA_FIN_LICENCIA) {
        this.FECHA_FIN_LICENCIA = FECHA_FIN_LICENCIA;
    }

    public String getFECHA_INICIO_LICENCIA() {
        return FECHA_INICIO_LICENCIA;
    }

    public void setFECHA_INICIO_LICENCIA(String FECHA_INICIO_LICENCIA) {
        this.FECHA_INICIO_LICENCIA = FECHA_INICIO_LICENCIA;
    }

    public String getLICENCIA_MEDICA() {
        return LICENCIA_MEDICA;
    }

    public void setLICENCIA_MEDICA(String LICENCIA_MEDICA) {
        this.LICENCIA_MEDICA = LICENCIA_MEDICA;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getAPELLIDO_PATERNO() {
        return APELLIDO_PATERNO;
    }

    public void setAPELLIDO_PATERNO(String APELLIDO_PATERNO) {
        this.APELLIDO_PATERNO = APELLIDO_PATERNO;
    }

    public String getAPELLIDO_MATERNO() {
        return APELLIDO_MATERNO;
    }

    public void setAPELLIDO_MATERNO(String APELLIDO_MATERNO) {
        this.APELLIDO_MATERNO = APELLIDO_MATERNO;
    }

    public datos_laborales(int id, int id_trabajador, String nombramiento, String t_contratacion, String l_adscrito,
                           String f_ingreso, String f_reingreso,String c_inasistencia, String observaciones, String f_fin_licencia,
                           String f_inicio_licencia, String l_medica, String nombre, String apaterno,String amaterno){
        setID(id);
        setID_TRABAJADOR(id_trabajador);
        setNOMBRAMIENTO(nombramiento);
        setTIPO_CONTRATACION(t_contratacion);
        setLUGAR_ADSCRIPCION(l_adscrito);
        setFECHA_INGRESO(f_ingreso);
        setFECHA_REINGRESO(f_reingreso);
        setCAUSA_INASISTENCIA(c_inasistencia);
        setOBSERVACIONES(observaciones);
        setFECHA_FIN_LICENCIA(f_fin_licencia);
        setFECHA_INICIO_LICENCIA(f_inicio_licencia);
        setLICENCIA_MEDICA(l_medica);
        setNOMBRE(nombre);
        setAPELLIDO_PATERNO(apaterno);
        setAPELLIDO_MATERNO(amaterno);
    }

    public datos_laborales(){

    }

}
