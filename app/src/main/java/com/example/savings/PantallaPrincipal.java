package com.example.savings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PantallaPrincipal extends AppCompatActivity {

    private TextView TextoMontoDinero;
    private ImageButton BotonOcultarSaldo;
    private ImageButton BotonNotificaciones;
    private ImageButton BotonAjustes;
    private Button BotonAgregarDinero;
    private Button BotonRetirarDinero;
    private Button BotonVerExtracto;
    private boolean SaldoVisible = true;
    private double SaldoActual = 0.00;
    private View PuntoRojoIndicador;

    private ArrayList<MovimientoModelo> HistorialMovimientos = new ArrayList<>();
    private ArrayList<NotificacionModelo> HistorialNotificaciones = new ArrayList<>();

    private final ActivityResultLauncher<Intent> LanzadorFormulario = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            Resultado -> {
                if (Resultado.getResultCode() == RESULT_OK && Resultado.getData() != null) {
                    double MontoRecibido = Resultado.getData().getDoubleExtra("MONTO", 0.0);
                    String Tipo = Resultado.getData().getStringExtra("TIPO");
                    String Concepto = Resultado.getData().getStringExtra("CONCEPTO");
                    if ("AGREGAR".equals(Tipo)) {
                        SaldoActual += MontoRecibido;
                    } else {
                        SaldoActual -= MontoRecibido;
                    }
                    HistorialMovimientos.add(0, new MovimientoModelo(Tipo, Concepto, MontoRecibido, SaldoActual));

                    String FechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
                    String MensajeNotificacion = Tipo + " de " + MontoRecibido + " Bs. en fecha " + FechaHora;
                    HistorialNotificaciones.add(0, new NotificacionModelo(MensajeNotificacion, System.currentTimeMillis()));
                    PuntoRojoIndicador.setVisibility(View.VISIBLE);
                    GestorNotificaciones.LanzarNotificacion(this, "Transacción Exitosa", MensajeNotificacion);

                    ActualizarTextoSaldo();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    android.content.pm.PackageManager.PERMISSION_GRANTED) {

                androidx.core.app.ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        TextoMontoDinero = findViewById(R.id.TextoMontoDinero);
        BotonOcultarSaldo = findViewById(R.id.BotonOcultarSaldo);
        BotonNotificaciones = findViewById(R.id.BotonNotificaciones);
        BotonAgregarDinero = findViewById(R.id.BotonAgregarDinero);
        BotonRetirarDinero = findViewById(R.id.BotonRetirarDinero);
        BotonVerExtracto = findViewById(R.id.BotonVerExtracto);
        PuntoRojoIndicador = findViewById(R.id.PuntoRojoIndicador);
        BotonAjustes = findViewById(R.id.BotonAjustes);

        BotonOcultarSaldo.setOnClickListener(Vista -> {
            SaldoVisible = !SaldoVisible;
            ActualizarTextoSaldo();
            BotonOcultarSaldo.setImageResource(SaldoVisible ? R.drawable.ic_visibility_off : R.drawable.ic_visibility_on);
        });

        BotonAgregarDinero.setOnClickListener(Vista -> {
            Intent IrAFormulario = new Intent(this, FormularioMovimientoActivity.class);
            IrAFormulario.putExtra("TIPO_MOVIMIENTO", "AGREGAR");
            LanzadorFormulario.launch(IrAFormulario);
        });

        BotonRetirarDinero.setOnClickListener(Vista -> {
            Intent IrAFormulario = new Intent(this, FormularioMovimientoActivity.class);
            IrAFormulario.putExtra("TIPO_MOVIMIENTO", "RETIRAR");
            LanzadorFormulario.launch(IrAFormulario);
        });

        BotonVerExtracto.setOnClickListener(Vista -> {
            Intent IrAExtracto = new Intent(this, ExtractoMovimientosActivity.class);
            IrAExtracto.putExtra("LISTA_MOVIMIENTOS", HistorialMovimientos);
            startActivity(IrAExtracto);
        });

        BotonNotificaciones.setOnClickListener(Vista -> {
            PuntoRojoIndicador.setVisibility(View.GONE);
            Intent IrANotificaciones = new Intent(this, NotificacionesActivity.class);
            IrANotificaciones.putExtra("LISTA_NOTIFICACIONES", HistorialNotificaciones);
            startActivity(IrANotificaciones);
        });

        BotonAjustes.setOnClickListener(v -> {
            startActivity(new Intent(this, AjustesActivity.class));
        });
    }
    private void ActualizarTextoSaldo() {
        if (SaldoVisible) {
            TextoMontoDinero.setText(String.format(Locale.getDefault(), "Bs. %.2f", SaldoActual));
        } else {
            TextoMontoDinero.setText("****");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ActualizarSaludoPersonalizado();
    }

    private void ActualizarSaludoPersonalizado() {
        SharedPreferences Preferencias = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        String Nombre = Preferencias.getString("Nombre", "");

        TextView TextoSaludoUsuario = findViewById(R.id.TextoSaludoUsuario);

        Calendar Cal = Calendar.getInstance();
        int Hora = Cal.get(Calendar.HOUR_OF_DAY);
        String MomentoDia;

        if (Hora >= 6 && Hora < 12) MomentoDia = "Buenos días";
        else if (Hora >= 12 && Hora < 19) MomentoDia = "Buenas tardes";
        else MomentoDia = "Buenas noches";

        if (Nombre.isEmpty()) {
            TextoSaludoUsuario.setText("¡Bienvenid@!");
        } else {
            TextoSaludoUsuario.setText(MomentoDia + ", " + Nombre);
        }
    };
}