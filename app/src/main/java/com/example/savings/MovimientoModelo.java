package com.example.savings;

import java.io.Serializable;

public class MovimientoModelo implements Serializable {
    private String TipoTransaccion;
    private String Concepto;
    private double Cantidad;
    private double SaldoResultante;
    private long FechaMilisegundos;

    public MovimientoModelo(String TipoTransaccion, String Concepto, double Cantidad, double SaldoResultante) {
        this.TipoTransaccion = TipoTransaccion;
        this.Concepto = Concepto;
        this.Cantidad = Cantidad;
        this.SaldoResultante = SaldoResultante;
        this.FechaMilisegundos = System.currentTimeMillis();
    }
    public String obtenerTipo() { return TipoTransaccion; }
    public String obtenerConcepto() { return Concepto; }
    public double obtenerCantidad() { return Cantidad; }
    public double obtenerSaldo() { return SaldoResultante; }
    public long obtenerFecha() { return FechaMilisegundos; }
}