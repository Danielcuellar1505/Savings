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

        // Recibir la lista
        ArrayList<NotificacionModelo> ListaRecibida =
                (ArrayList<NotificacionModelo>) getIntent().getSerializableExtra("LISTA_NOTIFICACIONES");

        if (ListaRecibida == null) ListaRecibida = new ArrayList<>();

        // Configurar RecyclerView
        RecyclerNotificaciones.setLayoutManager(new LinearLayoutManager(this));
        MiAdaptador = new AdaptadorNotificaciones(ListaRecibida);
        RecyclerNotificaciones.setAdapter(MiAdaptador);

        BotonRegresarNotif.setOnClickListener(v -> finish());
    }
}