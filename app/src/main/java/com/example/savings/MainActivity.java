package com.example.savings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private Executor EjecutorHilos;
    private BiometricPrompt AutenticadorBiometrico;
    private BiometricPrompt.PromptInfo ConfiguracionPrompt;
    private ImageButton BotonHuellaDigital;
    private EditText EntradaLoginPin;
    private Button BotonEntrarConPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BotonHuellaDigital = findViewById(R.id.BotonHuellaDigital);
        EntradaLoginPin = findViewById(R.id.EntradaLoginPin);
        BotonEntrarConPin = findViewById(R.id.BotonEntrarConPin);

        ConfigurarBiometria();

        CBaseDatos.obtenerInstancia().obtenerDatosUsuario((nombreCompleto, pinNube) -> {
            if (pinNube != null && !pinNube.isEmpty()) {
                EntradaLoginPin.setVisibility(View.VISIBLE);
                BotonEntrarConPin.setVisibility(View.VISIBLE);

                BotonEntrarConPin.setOnClickListener(v -> {
                    if (EntradaLoginPin.getText().toString().equals(pinNube)) {
                        IrAPantallaPrincipal();
                    } else {
                        Toast.makeText(this, "PIN Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                EntradaLoginPin.setVisibility(View.GONE);
                BotonEntrarConPin.setVisibility(View.GONE);
            }
        });
        AutenticadorBiometrico.authenticate(ConfiguracionPrompt);

        BotonHuellaDigital.setOnClickListener(Vista -> {
            AutenticadorBiometrico.authenticate(ConfiguracionPrompt);
        });
    }

    private void ConfigurarBiometria() {
        EjecutorHilos = ContextCompat.getMainExecutor(this);
        AutenticadorBiometrico = new BiometricPrompt(this, EjecutorHilos,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        IrAPantallaPrincipal();
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }
                });

        ConfiguracionPrompt = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Acceso Biométrico")
                .setSubtitle("Use su huella para ingresar")
                .setNegativeButtonText("Usar PIN de respaldo")
                .build();
    }

    private void IrAPantallaPrincipal() {
        Intent intent = new Intent(this, PantallaPrincipal.class);
        startActivity(intent);
        finish();
    }
}