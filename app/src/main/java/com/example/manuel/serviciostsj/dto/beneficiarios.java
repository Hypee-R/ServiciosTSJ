package com.example.manuel.serviciostsj.dto;

/**
 * Created by Manuel on 05/07/2017.
 */

public class beneficiarios {
    private int ID;
    private int ID_TRABAJADOR;
    private String NOMBRE;
    private String APATERNO;
    private String AMATERNO;
    private String CURP;
    private String DOMICILIO;
    private String MUNICIPIO;
    private String FECHA_NACIMIENTO;
    private String CORREO;
    private String TELEFONO_MOVIL;
    private String TELEFONO_FIJO;
    private String GRADO_ESTUDIOS;
    private String ESTATUS;
    private String PAIS;
    private String ESTADO;
    private String PARENTESCO;
    private String ESPECIALIDAD;
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

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getAPATERNO() {
        return APATERNO;
    }

    public void setAPATERNO(String APATERNO) {
        this.APATERNO = APATERNO;
    }

    public String getAMATERNO() {
        return AMATERNO;
    }

    public void setAMATERNO(String AMATERNO) {
        this.AMATERNO = AMATERNO;
    }

    public String getCURP() {
        return CURP;
    }

    public void setCURP(String CURP) {
        this.CURP = CURP;
    }

    public String getDOMICILIO() {
        return DOMICILIO;
    }

    public void setDOMICILIO(String DOMICILIO) {
        this.DOMICILIO = DOMICILIO;
    }

    public String getMUNICIPIO() {
        return MUNICIPIO;
    }

    public void setMUNICIPIO(String MUNICIPIO) {
        this.MUNICIPIO = MUNICIPIO;
    }

    public String getFECHA_NACIMIENTO() {
        return FECHA_NACIMIENTO;
    }

    public void setFECHA_NACIMIENTO(String FECHA_NACIMIENTO) {
        this.FECHA_NACIMIENTO = FECHA_NACIMIENTO;
    }

    public String getCORREO() {
        return CORREO;
    }

    public void setCORREO(String CORREO) {
        this.CORREO = CORREO;
    }

    public String getTELEFONO_MOVIL() {
        return TELEFONO_MOVIL;
    }

    public void setTELEFONO_MOVIL(String TELEFONO_MOVIL) {
        this.TELEFONO_MOVIL = TELEFONO_MOVIL;
    }

    public String getTELEFONO_FIJO() {
        return TELEFONO_FIJO;
    }

    public void setTELEFONO_FIJO(String TELEFONO_FIJO) {
        this.TELEFONO_FIJO = TELEFONO_FIJO;
    }

    public String getGRADO_ESTUDIOS() {
        return GRADO_ESTUDIOS;
    }

    public void setGRADO_ESTUDIOS(String GRADO_ESTUDIOS) {
        this.GRADO_ESTUDIOS = GRADO_ESTUDIOS;
    }

    public String getESTATUS() {
        return ESTATUS;
    }

    public void setESTATUS(String ESTATUS) {
        this.ESTATUS = ESTATUS;
    }

    public String getPAIS() {
        return PAIS;
    }

    public void setPAIS(String PAIS) {
        this.PAIS = PAIS;
    }

    public String getESTADO() {
        return ESTADO;
    }

    public void setESTADO(String ESTADO) {
        this.ESTADO = ESTADO;
    }

    public String getPARENTESCO() {
        return PARENTESCO;
    }

    public void setPARENTESCO(String PARENTESCO) {
        this.PARENTESCO = PARENTESCO;
    }

    public String getESPECIALIDAD() {
        return ESPECIALIDAD;
    }

    public void setESPECIALIDAD(String ESPECIALIDAD) {
        this.ESPECIALIDAD = ESPECIALIDAD;
    }

    public String getOBSERVACIONES() {
        return OBSERVACIONES;
    }

    public void setOBSERVACIONES(String OBSERVACIONES) {
        this.OBSERVACIONES = OBSERVACIONES;
    }

    public beneficiarios(int id, int id_trbjdr, String nombre, String apaterno, String amaterno, String curp, String domicilio,
                         String municipio, String f_nacimiento, String correo, String t_movil, String t_fijo, String g_estudios,
                         String estatus, String pais,String estado, String parentesco, String especialidad, String observaciones){
        setID(id);
        setID_TRABAJADOR(id_trbjdr);
        setNOMBRE(nombre);
        setAPATERNO(apaterno);
        setAMATERNO(amaterno);
        setCURP(curp);
        setDOMICILIO(domicilio);
        setMUNICIPIO(municipio);
        setFECHA_NACIMIENTO(f_nacimiento);
        setCORREO(correo);
        setTELEFONO_MOVIL(t_movil);
        setTELEFONO_FIJO(t_fijo);
        setGRADO_ESTUDIOS(g_estudios);
        setESTATUS(estatus);
        setPAIS(pais);
        setESTADO(estado);
        setPARENTESCO(parentesco);
        setESPECIALIDAD(especialidad);
        setOBSERVACIONES(observaciones);
    }

    public beneficiarios(){

    }

}
