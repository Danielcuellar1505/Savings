package com.example.savings;

import android.content.Intent;
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

import java.util.Locale;

public class PantallaPrincipal extends AppCompatActivity {

    private TextView TextoMontoDinero;
    private ImageButton BotonOcultarSaldo;
    private Button BotonAgregarDinero;
    private Button BotonRetirarDinero;
    private Button BotonVerExtracto;
    private boolean SaldoVisible = true;
    private double SaldoActual = 0.00;
    private java.util.ArrayList<MovimientoModelo> HistorialMovimientos = new java.util.ArrayList<>();

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

                    ActualizarTextoSaldo();
                }

            }

    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        TextoMontoDinero = findViewById(R.id.TextoMontoDinero);
        BotonOcultarSaldo = findViewById(R.id.BotonOcultarSaldo);
        BotonAgregarDinero = findViewById(R.id.BotonAgregarDinero);
        BotonRetirarDinero = findViewById(R.id.BotonRetirarDinero);
        BotonVerExtracto = findViewById(R.id.BotonVerExtracto);

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
    }
    private void ActualizarTextoSaldo() {
        if (SaldoVisible) {
            TextoMontoDinero.setText(String.format(Locale.getDefault(), "Bs. %.2f", SaldoActual));
        } else {
            TextoMontoDinero.setText("****");
        }
    }
}