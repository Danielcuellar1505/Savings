package com.example.savings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SeguridadActivity extends AppCompatActivity {

    private String nombreCompletoActual = "";
    private double saldoActual = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguridad);

        EditText EntradaNuevoPin = findViewById(R.id.EntradaNuevoPin);
        Button BotonGuardarPin = findViewById(R.id.BotonGuardarPin);

        CBaseDatos.obtenerInstancia().obtenerDatosUsuario((nombreCompleto, pin) -> {
            this.nombreCompletoActual = nombreCompleto;
            EntradaNuevoPin.setText(pin);
        });

        CBaseDatos.obtenerInstancia().obtenerSaldoGlobal(saldo -> {
            this.saldoActual = (saldo != null) ? saldo : 0.00;
        });

        BotonGuardarPin.setOnClickListener(v -> {
            String nuevoPin = EntradaNuevoPin.getText().toString();
            if (nuevoPin.length() == 4) {
                CBaseDatos.obtenerInstancia().guardarPerfil(nombreCompletoActual, nuevoPin, saldoActual);

                Toast.makeText(this, "Seguridad actualizada", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "El PIN debe ser de 4 dígitos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}