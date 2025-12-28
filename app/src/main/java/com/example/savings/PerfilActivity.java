package com.example.savings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    private String pinActual = "";
    private double saldoActual = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ImageButton BotonRegresar = findViewById(R.id.BotonRegresarPerfil);
        EditText EntradaNombre = findViewById(R.id.EntradaNombreCompleto);
        Button BotonEditar = findViewById(R.id.BotonEditarPerfil);
        Button BotonGuardar = findViewById(R.id.BotonGuardarPerfil);

        EntradaNombre.setEnabled(false);
        BotonGuardar.setVisibility(View.GONE);

        CBaseDatos.obtenerInstancia().obtenerDatosUsuario((nombreCompleto, pin) -> {
            this.pinActual = pin;
            if (nombreCompleto != null && !nombreCompleto.equals("Invitado")) {
                EntradaNombre.setText(nombreCompleto);
            } else {
                EntradaNombre.setText("");
            }
        });

        CBaseDatos.obtenerInstancia().obtenerSaldoGlobal(saldo -> {
            this.saldoActual = (saldo != null) ? saldo : 0.0;
        });

        BotonEditar.setOnClickListener(v -> {
            EntradaNombre.setEnabled(true);
            EntradaNombre.requestFocus();
            BotonGuardar.setVisibility(View.VISIBLE);
            BotonEditar.setVisibility(View.GONE);
            Toast.makeText(this, "Modo edición activado", Toast.LENGTH_SHORT).show();
        });

        BotonGuardar.setOnClickListener(v -> {
            String nuevoNombre = EntradaNombre.getText().toString().trim();

            if (!nuevoNombre.isEmpty()) {
                CBaseDatos.obtenerInstancia().guardarPerfil(nuevoNombre, pinActual, saldoActual);
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                EntradaNombre.setError("Escribe tu nombre completo");
            }
        });

        BotonRegresar.setOnClickListener(v -> finish());
    }
}