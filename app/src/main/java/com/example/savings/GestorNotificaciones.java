package com.example.savings;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class GestorNotificaciones {
    private static final String CANAL_ID = "canal_ahorros";

    public static void LanzarNotificacion(Context Contexto, String Titulo, String Contenido) {
        NotificationManager Manager = (NotificationManager) Contexto.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel Canal = new NotificationChannel(CANAL_ID, "Transacciones", NotificationManager.IMPORTANCE_DEFAULT);
            Manager.createNotificationChannel(Canal);
        }

        NotificationCompat.Builder Constructor = new NotificationCompat.Builder(Contexto, CANAL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(Titulo)
                .setContentText(Contenido)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Manager.notify((int) System.currentTimeMillis(), Constructor.build());
    }
}