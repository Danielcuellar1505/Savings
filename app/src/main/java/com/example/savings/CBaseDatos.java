package com.example.savings;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CBaseDatos {
    private static CBaseDatos Instancia;
    private final FirebaseFirestore db;
    private String idUsuarioActual = null;

    private final String COL_USUARIO = "Usuarios";
    private final String COL_MOVIMIENTOS = "Movimientos";
    private final String COL_NOTIFICACIONES = "Notificaciones";

    private CBaseDatos() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized CBaseDatos obtenerInstancia() {
        if (Instancia == null) {
            Instancia = new CBaseDatos();
        }
        return Instancia;
    }

    public interface DatosCargadosListener<T> { void onListaCargada(ArrayList<T> lista); }
    public interface OnSaldoReadListener { void onSaldoObtenido(Double saldo); }
    public interface OnUsuarioReadListener { void onUsuarioObtenido(String nombreCompleto, String pin); }

    public void guardarPerfil(String nombreCompleto, String pin, double saldo) {
        Map<String, Object> u = new HashMap<>();
        u.put("nombreCompleto", nombreCompleto);
        u.put("pin_seguridad", pin);
        u.put("saldo_actual", saldo);
        u.put("fecha_registro", FieldValue.serverTimestamp());

        if (idUsuarioActual != null) {
            db.collection(COL_USUARIO).document(idUsuarioActual)
                    .set(u, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        android.util.Log.d("Firestore", "Perfil anónimo actualizado a real");
                    });
        } else {
            db.collection(COL_USUARIO).add(u).addOnSuccessListener(doc -> {
                idUsuarioActual = doc.getId();
            });
        }
    }

    public void obtenerDatosUsuario(OnUsuarioReadListener listener) {
        db.collection(COL_USUARIO)
                .orderBy("fecha_registro", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null || snapshots.isEmpty()) {
                        listener.onUsuarioObtenido("Invitado", "");
                        return;
                    }
                    var doc = snapshots.getDocuments().get(0);
                    this.idUsuarioActual = doc.getId();
                    listener.onUsuarioObtenido(
                            doc.getString("nombreCompleto"),
                            doc.getString("pin_seguridad")
                    );
                });
    }
    public void obtenerSaldoGlobal(OnSaldoReadListener listener) {
        db.collection(COL_USUARIO)
                .orderBy("fecha_registro", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener((snapshots, e) -> {
                    if (snapshots != null && !snapshots.isEmpty()) {
                        var doc = snapshots.getDocuments().get(0);
                        this.idUsuarioActual = doc.getId();
                        Double saldo = doc.getDouble("saldo_actual");
                        listener.onSaldoObtenido(saldo != null ? saldo : 0.0);
                    } else {
                        Map<String, Object> anonimo = new HashMap<>();
                        anonimo.put("nombreCompleto", "Invitado");
                        anonimo.put("saldo_actual", 0.0);
                        anonimo.put("pin_seguridad", "");
                        anonimo.put("fecha_registro", FieldValue.serverTimestamp());

                        db.collection(COL_USUARIO).add(anonimo).addOnSuccessListener(doc -> {
                            this.idUsuarioActual = doc.getId();
                            listener.onSaldoObtenido(0.0);
                        });
                    }
                });
    }
    public void registrarMovimiento(MovimientoModelo movimiento) {
        db.collection(COL_MOVIMIENTOS).add(movimiento);
        if (idUsuarioActual != null) {
            db.collection(COL_USUARIO).document(idUsuarioActual)
                    .update("saldo_actual", movimiento.obtenerSaldo());
        }
    }

    public void cargarMovimientos(DatosCargadosListener<MovimientoModelo> listener) {
        db.collection(COL_MOVIMIENTOS)
                .orderBy("fecha_registro", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        android.util.Log.e("FirestoreError", e.getMessage());
                        return;
                    }
                    if (snapshots != null) {
                        ArrayList<MovimientoModelo> lista = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            lista.add(doc.toObject(MovimientoModelo.class));
                        }
                        listener.onListaCargada(lista);
                    }
                });
    }
    public void registrarNotificacion(NotificacionModelo notificacion) {
        db.collection(COL_NOTIFICACIONES).add(notificacion);
    }

    public void cargarNotificaciones(DatosCargadosListener<NotificacionModelo> listener) {
        db.collection(COL_NOTIFICACIONES)
                .orderBy("fecha_registro", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) return;
                    if (snapshots != null) {
                        ArrayList<NotificacionModelo> lista = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            lista.add(doc.toObject(NotificacionModelo.class));
                        }
                        listener.onListaCargada(lista);
                    }
                });
    }
}