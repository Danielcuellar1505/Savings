package com.example.savings;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NotificacionesActivity extends AppCompatActivity {

    private RecyclerView RecyclerNotificaciones;
    private AdaptadorNotificaciones MiAdaptador;
    private ImageButton BotonRegresarNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        BotonRegresarNotif = findViewById(R.id.BotonRegresarNotif);
        RecyclerNotificaciones = findViewById(R.id.RecyclerNotificaciones);

        RecyclerNotificaciones.setLayoutManager(new LinearLayoutManager(this));
        MiAdaptador = new AdaptadorNotificaciones(new ArrayList<>());
        RecyclerNotificaciones.setAdapter(MiAdaptador);

        CBaseDatos.obtenerInstancia().cargarNotificaciones(lista -> {
            if (lista != null) {
                MiAdaptador = new AdaptadorNotificaciones(lista);
                RecyclerNotificaciones.setAdapter(MiAdaptador);
            }
        });

        BotonRegresarNotif.setOnClickListener(v -> finish());
    }
}