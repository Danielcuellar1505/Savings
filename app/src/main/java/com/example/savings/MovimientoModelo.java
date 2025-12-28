package com.example.savings;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Date;

public class MovimientoModelo implements Serializable {
    public String tipo;
    public String concepto;
    public double monto;
    public double saldo_resultante;
    public Date fecha_registro;

    public MovimientoModelo(){}
    public MovimientoModelo(String tipo, String concepto, double monto, double saldo_resultante) {
        this.tipo = tipo;
        this.concepto = concepto;
        this.monto = monto;
        this.saldo_resultante = saldo_resultante;
        this.fecha_registro = new Date();
    }

    public String obtenerTipo() { return tipo; }
    public String obtenerConcepto() { return concepto; }
    public double obtenerCantidad() { return monto; }
    public double obtenerSaldo() { return saldo_resultante; }

    public long obtenerFecha() {
        return (fecha_registro != null) ? fecha_registro.getTime() : 0;
    }
}