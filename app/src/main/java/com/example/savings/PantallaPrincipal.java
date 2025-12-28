package com.example.savings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PantallaPrincipal extends AppCompatActivity {

    private TextView TextoMontoDinero;
    private ImageButton BotonOcultarSaldo, BotonNotificaciones, BotonAjustes;
    private Button BotonAgregarDinero, BotonRetirarDinero, BotonVerExtracto;
    private View PuntoRojoIndicador;

    private boolean SaldoVisible = true;
    private double SaldoActual = 0.00;
    private ArrayList<MovimientoModelo> HistorialMovimientos = new ArrayList<>();
    private ArrayList<NotificacionModelo> HistorialNotificaciones = new ArrayList<>();

    private final ActivityResultLauncher<Intent> LanzadorFormulario = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            Resultado -> {
                if (Resultado.getResultCode() == RESULT_OK && Resultado.getData() != null) {
                    double MontoRecibido = Resultado.getData().getDoubleExtra("MONTO", 0.0);
                    String Tipo = Resultado.getData().getStringExtra("TIPO");
                    String Concepto = Resultado.getData().getStringExtra("CONCEPTO");
                    if ("RETIRAR".equals(Tipo) && MontoRecibido > SaldoActual) {
                        Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_LONG).show();
                        return;
                    }
                    double nuevoSaldoCalculado = ("AGREGAR".equals(Tipo))
                            ? SaldoActual + MontoRecibido
                            : SaldoActual - MontoRecibido;
                    MovimientoModelo nuevoMov = new MovimientoModelo(Tipo, Concepto, MontoRecibido, nuevoSaldoCalculado);
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
                    String fechaFormateada = sdf.format(nuevoMov.fecha_registro);
                    String mensajeNotif = Tipo + " " + MontoRecibido + " Bs. en fecha " + fechaFormateada;
                    NotificacionModelo nuevaNotif = new NotificacionModelo(mensajeNotif);
                    CBaseDatos.obtenerInstancia().registrarMovimiento(nuevoMov);
                    CBaseDatos.obtenerInstancia().registrarNotificacion(nuevaNotif);
                    GestorNotificaciones.LanzarNotificacion(this, "Transacción Exitosa",
                            Tipo + ": " + MontoRecibido + " Bs. en fecha " + fechaFormateada);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
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

        CBaseDatos.obtenerInstancia().obtenerSaldoGlobal(saldo -> {
            this.SaldoActual = (saldo != null) ? saldo : 0.00;
            ActualizarTextoSaldo();
        });

        CBaseDatos.obtenerInstancia().cargarMovimientos(lista -> {
            if (lista != null) this.HistorialMovimientos = lista;
        });

        CBaseDatos.obtenerInstancia().cargarNotificaciones(lista -> {
            if (lista != null) {
                this.HistorialNotificaciones = lista;
                if (!lista.isEmpty()) PuntoRojoIndicador.setVisibility(View.VISIBLE);
            }
        });

        BotonOcultarSaldo.setOnClickListener(v -> {
            SaldoVisible = !SaldoVisible;
            ActualizarTextoSaldo();
            BotonOcultarSaldo.setImageResource(SaldoVisible ? R.drawable.ic_visibility_off : R.drawable.ic_visibility_on);
        });

        BotonAgregarDinero.setOnClickListener(v -> abrirFormulario("AGREGAR"));
        BotonRetirarDinero.setOnClickListener(v -> abrirFormulario("RETIRAR"));

        BotonVerExtracto.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExtractoMovimientosActivity.class);
            intent.putExtra("LISTA_MOVIMIENTOS", HistorialMovimientos);
            startActivity(intent);
        });

        BotonNotificaciones.setOnClickListener(v -> {
            PuntoRojoIndicador.setVisibility(View.GONE);
            Intent intent = new Intent(this, NotificacionesActivity.class);
            intent.putExtra("LISTA_NOTIFICACIONES", HistorialNotificaciones);
            startActivity(intent);
        });

        BotonAjustes.setOnClickListener(v -> startActivity(new Intent(this, AjustesActivity.class)));
    }

    private void abrirFormulario(String tipo) {
        Intent intent = new Intent(this, FormularioMovimientoActivity.class);
        intent.putExtra("TIPO_MOVIMIENTO", tipo);
        LanzadorFormulario.launch(intent);
    }

    private void ActualizarTextoSaldo() {
        if (SaldoVisible) TextoMontoDinero.setText(String.format(Locale.getDefault(), "Bs. %.2f", SaldoActual));
        else TextoMontoDinero.setText("****");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActualizarSaludoPersonalizado();
    }

    private void ActualizarSaludoPersonalizado() {
        CBaseDatos.obtenerInstancia().obtenerDatosUsuario((nombreCompleto, pin) -> {
            TextView TextoSaludoUsuario = findViewById(R.id.TextoSaludoUsuario);
            if (nombreCompleto == null || nombreCompleto.equals("Invitado")) {
                TextoSaludoUsuario.setText("¡Bienvenid@!");
            } else {
                int Hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                String Momento = (Hora >= 6 && Hora < 12) ? "Buenos días" : (Hora >= 12 && Hora < 19) ? "Buenas tardes" : "Buenas noches";
                TextoSaludoUsuario.setText(Momento + ", " + nombreCompleto);
            }
        });
    }
}