package com.example.savings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        EditText EntradaNombre = findViewById(R.id.EntradaNombre);
        EditText EntradaApellido = findViewById(R.id.EntradaApellido);
        Button BotonGuardar = findViewById(R.id.BotonGuardarPerfil);

        BotonGuardar.setOnClickListener(v -> {
            String Nombre = EntradaNombre.getText().toString().trim();
            String Apellido = EntradaApellido.getText().toString().trim();

            if (!Nombre.isEmpty()) {
                SharedPreferences.Editor Editor = getSharedPreferences("DatosUsuario", MODE_PRIVATE).edit();
                Editor.putString("Nombre", Nombre);
                Editor.putString("Apellido", Apellido);
                Editor.apply();
                Toast.makeText(this, "Perfil Guardado", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}