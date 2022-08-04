package com.example.manuel.serviciostsj.dto;

/**
 * Created by Manuel on 08/06/2017.
 */

public class usuarios {
    private int ID;
    private String NOMBRE;
    private String APATERNO;
    private String AMATERNO;
    private String USUARIO;
    private String CONTRASEÑA;
    private String PERFIL;
    private int HABILITADO;
    private String RFC;
    private String CURP;
    private String DOMICILIO;
    private String ESTADO;
    private String PAIS;
    private String N_CEDULA;
    private String CORREO;
    private String T_FIJO;
    private String T_MOVIL;
    private String G_ESTUDIOS;
    private String ENTIDAD;
    private String PUESTO;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public String getUSUARIO() {
        return USUARIO;
    }

    public void setUSUARIO(String USUARIO) {
        this.USUARIO = USUARIO;
    }

    public String getCONTRASEÑA() {
        return CONTRASEÑA;
    }

    public void setCONTRASEÑA(String CONTRASEÑA) {
        this.CONTRASEÑA = CONTRASEÑA;
    }

    public String getPERFIL() {
        return PERFIL;
    }

    public void setPERFIL(String PERFIL) {
        this.PERFIL = PERFIL;
    }

    public int getHABILITADO() {
        return HABILITADO;
    }

    public void setHABILITADO(int HABILITADO) {
        this.HABILITADO = HABILITADO;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
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

    public String getESTADO() {
        return ESTADO;
    }

    public void setESTADO(String ESTADO) {
        this.ESTADO = ESTADO;
    }

    public String getPAIS() {
        return PAIS;
    }

    public void setPAIS(String PAIS) {
        this.PAIS = PAIS;
    }

    public String getN_CEDULA() {
        return N_CEDULA;
    }

    public void setN_CEDULA(String n_CEDULA) {
        N_CEDULA = n_CEDULA;
    }

    public String getCORREO() {
        return CORREO;
    }

    public void setCORREO(String CORREO) {
        this.CORREO = CORREO;
    }

    public String getT_FIJO() {
        return T_FIJO;
    }

    public void setT_FIJO(String t_FIJO) {
        T_FIJO = t_FIJO;
    }

    public String getT_MOVIL() {
        return T_MOVIL;
    }

    public void setT_MOVIL(String t_MOVIL) {
        T_MOVIL = t_MOVIL;
    }

    public String getG_ESTUDIOS() {
        return G_ESTUDIOS;
    }

    public void setG_ESTUDIOS(String g_ESTUDIOS) {
        G_ESTUDIOS = g_ESTUDIOS;
    }

    public String getENTIDAD() {
        return ENTIDAD;
    }

    public void setENTIDAD(String ENTIDAD) {
        this.ENTIDAD = ENTIDAD;
    }

    public String getPUESTO() {
        return PUESTO;
    }

    public void setPUESTO(String PUESTO) {
        this.PUESTO = PUESTO;
    }

    public usuarios(int id, String nombre, String Apaterno, String Amaterno, String Usuario, String Contraseña, String Perfil, int Habilitado,
                    String rfc,String curp,String domicilio, String estado, String pais, String n_cedula,String correo,String t_fijo,String t_movil,
                    String g_estudios,String entidad, String puesto){
        setID(id);
        setNOMBRE(nombre);
        setAPATERNO(Apaterno);
        setAMATERNO(Amaterno);
        setUSUARIO(Usuario);
        setCONTRASEÑA(Contraseña);
        setPERFIL(Perfil);
        setHABILITADO(Habilitado);
        setRFC(rfc);
        setCURP(curp);
        setDOMICILIO(domicilio);
        setESTADO(estado);
        setPAIS(pais);
        setN_CEDULA(n_cedula);
        setCORREO(correo);
        setT_FIJO(t_fijo);
        setT_MOVIL(t_movil);
        setG_ESTUDIOS(g_estudios);
        setENTIDAD(entidad);
        setPUESTO(puesto);
    }

    public usuarios(){

    }

}
