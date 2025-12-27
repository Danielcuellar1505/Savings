package com.example.savings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdaptadorNotificaciones extends RecyclerView.Adapter<AdaptadorNotificaciones.ViewHolder> {

    private ArrayList<NotificacionModelo> ListaNotificaciones;

    public AdaptadorNotificaciones(ArrayList<NotificacionModelo> ListaNotificaciones) {
        this.ListaNotificaciones = ListaNotificaciones;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup Padre, int ViewType) {
        View Vista = LayoutInflater.from(Padre.getContext()).inflate(R.layout.item_notificacion, Padre, false);
        return new ViewHolder(Vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder Holder, int Posicion) {
        NotificacionModelo Notificacion = ListaNotificaciones.get(Posicion);
        Holder.TextoNotificacion.setText(Notificacion.obtenerMensaje());
    }

    @Override
    public int getItemCount() {
        return (ListaNotificaciones != null) ? ListaNotificaciones.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView TextoNotificacion;

        public ViewHolder(@NonNull View VistaItem) {
            super(VistaItem);
            TextoNotificacion = VistaItem.findViewById(R.id.TextoNotificacion);
        }
    }
}