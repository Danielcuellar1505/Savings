package com.example.savings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdaptadorMovimiento extends RecyclerView.Adapter<AdaptadorMovimiento.ViewHolder> {

    private ArrayList<MovimientoModelo> ListaMovimientos;
    private ArrayList<MovimientoModelo> ListaCopiaBusqueda;

    public AdaptadorMovimiento(ArrayList<MovimientoModelo> ListaMovimientos) {
        this.ListaMovimientos = ListaMovimientos;
        this.ListaCopiaBusqueda = new ArrayList<>(ListaMovimientos);
    }
    public void ActualizarLista(ArrayList<MovimientoModelo> NuevaLista) {
        this.ListaMovimientos = NuevaLista;
        this.ListaCopiaBusqueda = new ArrayList<>(NuevaLista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup Padre, int ViewType) {
        View Vista = LayoutInflater.from(Padre.getContext()).inflate(R.layout.item_movimiento, Padre, false);
        return new ViewHolder(Vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder Holder, int Posicion) {
        MovimientoModelo MovimientoActual = ListaMovimientos.get(Posicion);
        String FechaActualFormateada = FormatearFecha(MovimientoActual.obtenerFecha());

        if (Posicion == 0) {
            Holder.EtiquetaFechaCabecera.setVisibility(View.VISIBLE);
            Holder.EtiquetaFechaCabecera.setText(FechaActualFormateada);
        } else {
            MovimientoModelo MovimientoAnterior = ListaMovimientos.get(Posicion - 1);
            String FechaAnteriorFormateada = FormatearFecha(MovimientoAnterior.obtenerFecha());

            if (FechaActualFormateada.equals(FechaAnteriorFormateada)) {
                Holder.EtiquetaFechaCabecera.setVisibility(View.GONE);
            } else {
                Holder.EtiquetaFechaCabecera.setVisibility(View.VISIBLE);
                Holder.EtiquetaFechaCabecera.setText(FechaActualFormateada);
            }
        }

        Holder.EtiquetaTipoTransaccion.setText(MovimientoActual.obtenerTipo());
        Holder.EtiquetaConcepto.setText(MovimientoActual.obtenerConcepto());

        if (MovimientoActual.obtenerTipo().equals("AGREGAR")) {
            Holder.EtiquetaCantidad.setText(String.format(Locale.getDefault(), "+ Bs. %.2f", MovimientoActual.obtenerCantidad()));
            Holder.EtiquetaCantidad.setTextColor(android.graphics.Color.parseColor("#1ABC9C"));
        } else {
            Holder.EtiquetaCantidad.setText(String.format(Locale.getDefault(), "- Bs. %.2f", MovimientoActual.obtenerCantidad()));
            Holder.EtiquetaCantidad.setTextColor(android.graphics.Color.parseColor("#E74C3C"));
        }

        Holder.EtiquetaSaldoResultante.setText(String.format(Locale.getDefault(), "Saldo: Bs. %.2f", MovimientoActual.obtenerSaldo()));
    }

    @Override
    public int getItemCount() {
        return ListaMovimientos.size();
    }

    public void FiltrarPorMonto(String TextoMonto) {
        ListaMovimientos.clear();
        if (TextoMonto.isEmpty()) {
            ListaMovimientos.addAll(ListaCopiaBusqueda);
        } else {
            for (MovimientoModelo Item : ListaCopiaBusqueda) {
                String ValorMonto = String.valueOf(Item.obtenerCantidad());
                if (ValorMonto.contains(TextoMonto)) {
                    ListaMovimientos.add(Item);
                }
            }
        }
        notifyDataSetChanged();
    }

    private String FormatearFecha(long Milisegundos) {
        SimpleDateFormat FormatoSimple = new SimpleDateFormat("EEEE, d 'de' MMMM", Locale.getDefault());
        return FormatoSimple.format(new Date(Milisegundos));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView EtiquetaTipoTransaccion, EtiquetaCantidad, EtiquetaConcepto, EtiquetaSaldoResultante, EtiquetaFechaCabecera;

        public ViewHolder(@NonNull View VistaItem) {
            super(VistaItem);
            EtiquetaTipoTransaccion = VistaItem.findViewById(R.id.EtiquetaTipoTransaccion);
            EtiquetaCantidad = VistaItem.findViewById(R.id.EtiquetaCantidad);
            EtiquetaConcepto = VistaItem.findViewById(R.id.EtiquetaConcepto);
            EtiquetaSaldoResultante = VistaItem.findViewById(R.id.EtiquetaSaldoResultante);
            EtiquetaFechaCabecera = VistaItem.findViewById(R.id.EtiquetaFechaCabecera);
        }
    }
}