package com.example.savings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class AjustesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        findViewById(R.id.BotonRegresarAjustes).setOnClickListener(v -> finish());

        findViewById(R.id.TarjetaPerfil).setOnClickListener(v ->
                startActivity(new Intent(this, PerfilActivity.class)));

        findViewById(R.id.TarjetaSeguridad).setOnClickListener(v ->
                startActivity(new Intent(this, SeguridadActivity.class)));
    }
}