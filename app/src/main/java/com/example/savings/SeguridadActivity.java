package com.example.savings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SeguridadActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguridad);

        EditText EntradaNuevoPin = findViewById(R.id.EntradaNuevoPin);
        Button BotonGuardarPin = findViewById(R.id.BotonGuardarPin);

        BotonGuardarPin.setOnClickListener(v -> {
            String Pin = EntradaNuevoPin.getText().toString();
            if (Pin.length() == 4) {
                SharedPreferences.Editor Editor = getSharedPreferences("DatosUsuario", MODE_PRIVATE).edit();
                Editor.putString("PIN", Pin);
                Editor.apply();
                Toast.makeText(this, "PIN configurado con éxito", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "El PIN debe ser de 4 dígitos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}