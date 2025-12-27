package com.example.savings;

import android.content.Intent;
import android.content.SharedPreferences;
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

        SharedPreferences Preferencias = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        String PinGuardado = Preferencias.getString("PIN", "");

        if (!PinGuardado.isEmpty()) {
            EntradaLoginPin.setVisibility(View.VISIBLE);
            BotonEntrarConPin.setVisibility(View.VISIBLE);

            BotonEntrarConPin.setOnClickListener(v -> {
                String PinIngresado = EntradaLoginPin.getText().toString();
                if (PinIngresado.equals(PinGuardado)) {
                    IrAPantallaPrincipal();
                } else {
                    Toast.makeText(this, "PIN Incorrecto", Toast.LENGTH_SHORT).show();
                }
            });
        }
        AutenticadorBiometrico.authenticate(ConfiguracionPrompt);

        BotonHuellaDigital.setOnClickListener(Vista -> {
            AutenticadorBiometrico.authenticate(ConfiguracionPrompt);
        });
    }

    private void ConfigurarBiometria() {
        EjecutorHilos = ContextCompat.getMainExecutor(this);
        AutenticadorBiometrico = new BiometricPrompt(this, EjecutorHilos, new BiometricPrompt.AuthenticationCallback() {
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
                .setTitle("Ingreso Seguro")
                .setSubtitle("Use su huella o el PIN configurado")
                .setNegativeButtonText("Cancelar")
                .build();
    }

    private void IrAPantallaPrincipal() {
        Toast.makeText(this, "¡Acceso Concedido!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PantallaPrincipal.class);
        startActivity(intent);
        finish();
    }
}