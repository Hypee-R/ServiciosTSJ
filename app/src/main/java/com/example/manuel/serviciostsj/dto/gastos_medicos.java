package com.example.manuel.serviciostsj.dto;

/**
 * Created by Manuel on 05/07/2017.
 */

public class gastos_medicos {
    private int ID;
    private int ID_BENEFICIARIO;
    private int ID_TRABAJADOR;
    private double CANTIDAD_ACUMULADA_TITULAR;
    private double CANTIDAD_ACUMULADA_BENEFICIARIO;
    private double CANTIDAD_PAGAR;
    private String CONCEPTO;
    private String COMPROBANTE;
    private String ESTATUS;
    private String FECHA;
    private String NUMERO_COMPROBANTE;
    private String OBSERVACIONES;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID_BENEFICIARIO() {
        return ID_BENEFICIARIO;
    }

    public void setID_BENEFICIARIO(int ID_BENEFICIARIO) {
        this.ID_BENEFICIARIO = ID_BENEFICIARIO;
    }

    public int getID_TRABAJADOR() {
        return ID_TRABAJADOR;
    }

    public void setID_TRABAJADOR(int ID_TRABAJADOR) {
        this.ID_TRABAJADOR = ID_TRABAJADOR;
    }

    public double getCANTIDAD_ACUMULADA_TITULAR() {
        return CANTIDAD_ACUMULADA_TITULAR;
    }

    public void setCANTIDAD_ACUMULADA_TITULAR(double CANTIDAD_ACUMULADA_TITULAR) {
        this.CANTIDAD_ACUMULADA_TITULAR = CANTIDAD_ACUMULADA_TITULAR;
    }

    public double getCANTIDAD_ACUMULADA_BENEFICIARIO() {
        return CANTIDAD_ACUMULADA_BENEFICIARIO;
    }

    public void setCANTIDAD_ACUMULADA_BENEFICIARIO(double CANTIDAD_ACUMULADA_BENEFICIARIO) {
        this.CANTIDAD_ACUMULADA_BENEFICIARIO = CANTIDAD_ACUMULADA_BENEFICIARIO;
    }

    public double getCANTIDAD_PAGAR() {
        return CANTIDAD_PAGAR;
    }

    public void setCANTIDAD_PAGAR(double CANTIDAD_PAGAR) {
        this.CANTIDAD_PAGAR = CANTIDAD_PAGAR;
    }

    public String getCONCEPTO() {
        return CONCEPTO;
    }

    public void setCONCEPTO(String CONCEPTO) {
        this.CONCEPTO = CONCEPTO;
    }

    public String getCOMPROBANTE() {
        return COMPROBANTE;
    }

    public void setCOMPROBANTE(String COMPROBANTE) {
        this.COMPROBANTE = COMPROBANTE;
    }

    public String getESTATUS() {
        return ESTATUS;
    }

    public void setESTATUS(String ESTATUS) {
        this.ESTATUS = ESTATUS;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }

    public String getNUMERO_COMPROBANTE() {
        return NUMERO_COMPROBANTE;
    }

    public void setNUMERO_COMPROBANTE(String NUMERO_COMPROBANTE) {
        this.NUMERO_COMPROBANTE = NUMERO_COMPROBANTE;
    }

    public String getOBSERVACIONES() {
        return OBSERVACIONES;
    }

    public void setOBSERVACIONES(String OBSERVACIONES) {
        this.OBSERVACIONES = OBSERVACIONES;
    }

    public gastos_medicos(int id, int id_beneficiario, int id_trbjdr, double cant_acum_titular, double cant_acum_bene, double cant_pagar,
                          String concepto,String comprobante, String estatus, String fecha, String n_comprobante, String observaciones){
        setID(id);
        setID_BENEFICIARIO(id_beneficiario);
        setID_TRABAJADOR(id_trbjdr);
        setCANTIDAD_ACUMULADA_TITULAR(cant_acum_titular);
        setCANTIDAD_ACUMULADA_BENEFICIARIO(cant_acum_bene);
        setCANTIDAD_PAGAR(cant_pagar);
        setCONCEPTO(concepto);
        setCOMPROBANTE(comprobante);
        setESTATUS(estatus);
        setFECHA(fecha);
        setNUMERO_COMPROBANTE(n_comprobante);
        setOBSERVACIONES(observaciones);

    }

    public gastos_medicos(){

    }

}
