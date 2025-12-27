package com.example.savings;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ExtractoMovimientosActivity extends AppCompatActivity {

    private RecyclerView ListaRecurrenteMovimientos;
    private AdaptadorMovimiento MiAdaptador;
    private ImageButton BotonRegresarExtracto;
    private ImageButton BotonAbrirFiltros;
    private EditText EntradaBusquedaMonto;
    private ArrayList<MovimientoModelo> ListaRecibida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extracto_movimientos);

        BotonRegresarExtracto = findViewById(R.id.BotonRegresarExtracto);
        ListaRecurrenteMovimientos = findViewById(R.id.ListaRecurrenteMovimientos);
        BotonAbrirFiltros = findViewById(R.id.BotonAbrirFiltros);
        ListaRecibida = (ArrayList<MovimientoModelo>) getIntent().getSerializableExtra("LISTA_MOVIMIENTOS");
        if (ListaRecibida == null) {
            ListaRecibida = new ArrayList<>();
        }
        ConfigurarLista();
        ConfigurarBuscador();
        BotonAbrirFiltros.setOnClickListener(v -> MostrarModalFiltros());

        BotonRegresarExtracto.setOnClickListener(Vista -> {
            finish();
        });
    }

    private void ConfigurarLista() {
        ListaRecurrenteMovimientos.setLayoutManager(new LinearLayoutManager(this));
        MiAdaptador = new AdaptadorMovimiento(new ArrayList<>(ListaRecibida));
        ListaRecurrenteMovimientos.setAdapter(MiAdaptador);
    }

    private void ConfigurarBuscador() {
        EntradaBusquedaMonto = findViewById(R.id.EntradaBusquedaMonto);

        EntradaBusquedaMonto.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence Secuencia, int Inicio, int Conteo, int Despues) {
            }

            @Override
            public void onTextChanged(CharSequence Secuencia, int Inicio, int Antes, int Conteo) {
                if (MiAdaptador != null) {
                    MiAdaptador.FiltrarPorMonto(Secuencia.toString());
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable Editable) {
            }
        });
    }
    private void MostrarModalFiltros() {
        BottomSheetDialog DialogoFiltro = new BottomSheetDialog(this);
        View VistaModal = getLayoutInflater().inflate(R.layout.layout_modal_filtros, null);
        LinearLayout Contenedor = VistaModal.findViewById(R.id.ContenedorOpcionesFiltro);

        String[] FiltrosFijos = {"Todos", "Hoy", "Ayer", "Mes Actual"};
        for (String Nombre : FiltrosFijos) {
            Contenedor.addView(CrearBotonFiltroEnModal(Nombre, DialogoFiltro));
        }
        if (!ListaRecibida.isEmpty()) {
            Calendar CalendarioCursor = Calendar.getInstance();
            CalendarioCursor.setTimeInMillis(ListaRecibida.get(ListaRecibida.size() - 1).obtenerFecha());
            Calendar CalendarioHoy = Calendar.getInstance();

            while (CalendarioCursor.before(CalendarioHoy) || EsMismoMes(CalendarioCursor, CalendarioHoy)) {
                String NombreMes = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(CalendarioCursor.getTime());
                Contenedor.addView(CrearBotonFiltroEnModal(NombreMes, DialogoFiltro));
                CalendarioCursor.add(Calendar.MONTH, 1);
            }
        }

        DialogoFiltro.setContentView(VistaModal);
        DialogoFiltro.show();
    }

    private Button CrearBotonFiltroEnModal(String Texto, BottomSheetDialog Dialogo) {
        Button Boton = new Button(this, null, android.R.attr.borderlessButtonStyle);
        Boton.setText(Texto);
        Boton.setAllCaps(false);
        Boton.setGravity(android.view.Gravity.START | android.view.Gravity.CENTER_VERTICAL);
        Boton.setTextColor(Color.parseColor("#2C3E50"));

        Boton.setOnClickListener(v -> {
            if (Texto.equals("Todos")) {
                MiAdaptador.ActualizarLista(new ArrayList<>(ListaRecibida));
            } else {
                FiltrarPorFecha(Texto);
            }
            Dialogo.dismiss();
        });
        return Boton;
    }

    private void FiltrarPorFecha(String Criterio) {
        ArrayList<MovimientoModelo> ListaFiltrada = new ArrayList<>();
        Calendar CalItem = Calendar.getInstance();
        Calendar CalReferencia = Calendar.getInstance();

        for (MovimientoModelo Item : ListaRecibida) {
            CalItem.setTimeInMillis(Item.obtenerFecha());

            if (Criterio.equals("Hoy")) {
                if (EsMismoDia(CalItem, CalReferencia)) ListaFiltrada.add(Item);
            } else if (Criterio.equals("Ayer")) {
                Calendar CalAyer = Calendar.getInstance();
                CalAyer.add(Calendar.DATE, -1);
                if (EsMismoDia(CalItem, CalAyer)) ListaFiltrada.add(Item);
            } else if (Criterio.equals("Mes Actual")) {
                if (EsMismoMes(CalItem, CalReferencia)) ListaFiltrada.add(Item);
            } else {
                String FechaItem = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(CalItem.getTime());
                if (FechaItem.equals(Criterio)) {
                    ListaFiltrada.add(Item);
                }
            }
        }
        MiAdaptador.ActualizarLista(ListaFiltrada);
    }

    private boolean EsMismoDia(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean EsMismoMes(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }
}