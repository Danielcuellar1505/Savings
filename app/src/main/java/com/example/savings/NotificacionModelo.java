package com.example.savings;

import java.io.Serializable;
import java.util.Date;

public class NotificacionModelo implements Serializable {
    public String mensaje;
    public Date fecha_registro;
    public NotificacionModelo() {}

    public NotificacionModelo(String mensaje) {
        this.mensaje = mensaje;
        this.fecha_registro = new Date();
    }
    public String obtenerMensaje() { return mensaje; }
    public long obtenerFecha() {
        return (fecha_registro != null) ? fecha_registro.getTime() : 0;
    }
}