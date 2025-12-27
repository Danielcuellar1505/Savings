package com.example.savings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BotonHuellaDigital = findViewById(R.id.BotonHuellaDigital);

        EjecutorHilos = ContextCompat.getMainExecutor(this);
        AutenticadorBiometrico = new BiometricPrompt(MainActivity.this, EjecutorHilos, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int CodigoError, @NonNull CharSequence CadenaError) {
                super.onAuthenticationError(CodigoError, CadenaError);
                Toast.makeText(MainActivity.this, "Error: " + CadenaError, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult ResultadoAutenticacion) {
                super.onAuthenticationSucceeded(ResultadoAutenticacion);
                Toast.makeText(MainActivity.this, "¡Acceso Concedido!", Toast.LENGTH_SHORT).show();
                Intent IrAPantallaPrincipal = new Intent(MainActivity.this, PantallaPrincipal.class);
                startActivity(IrAPantallaPrincipal);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(MainActivity.this, "Huella no reconocida", Toast.LENGTH_SHORT).show();
            }
        });

        ConfiguracionPrompt = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Ingreso Seguro")
                .setSubtitle("Coloque su huella en el sensor")
                .setNegativeButtonText("Cancelar")
                .build();

        AutenticadorBiometrico.authenticate(ConfiguracionPrompt);

        BotonHuellaDigital.setOnClickListener(Vista -> {
            AutenticadorBiometrico.authenticate(ConfiguracionPrompt);
        });
    }
}