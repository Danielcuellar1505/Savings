package com.example.savings;

import java.io.Serializable;

public class NotificacionModelo implements Serializable {
    private String Mensaje;
    private long FechaMilis;

    public NotificacionModelo(String Mensaje, long FechaMilis) {
        this.Mensaje = Mensaje;
        this.FechaMilis = FechaMilis;
    }

    public String obtenerMensaje() { return Mensaje; }
    public long obtenerFecha() { return FechaMilis; }
}