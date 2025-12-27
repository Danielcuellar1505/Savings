package com.example.savings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FormularioMovimientoActivity extends AppCompatActivity {

    private TextView TextoTituloFormulario;
    private EditText EntradaMonto;
    private EditText EntradaReferencia;
    private Button BotonConfirmarMovimiento;
    private String TipoMovimiento;
    private ImageButton BotonRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_movimiento);

        TipoMovimiento = getIntent().getStringExtra("TIPO_MOVIMIENTO");
        BotonRegresar = findViewById(R.id.BotonRegresar);
        TextoTituloFormulario = findViewById(R.id.TextoTituloFormulario);
        EntradaMonto = findViewById(R.id.EntradaMonto);
        EntradaReferencia = findViewById(R.id.EntradaReferencia);
        BotonConfirmarMovimiento = findViewById(R.id.BotonConfirmarMovimiento);


        BotonRegresar.setOnClickListener(Vista -> {
            finish();
        });

        BotonConfirmarMovimiento.setOnClickListener(Vista -> {
            String TextoMonto = EntradaMonto.getText().toString();
            String TextoConcepto = EntradaReferencia.getText().toString();
            if (!TextoMonto.isEmpty()) {
                double MontoIngresado = Double.parseDouble(TextoMonto);
                if (TextoConcepto.isEmpty()) {
                    TextoConcepto = (TipoMovimiento.equals("AGREGAR")) ? "Ahorro sin nombre" : "Gasto sin nombre";
                }
                Intent DatosRetorno = new Intent();
                DatosRetorno.putExtra("MONTO", MontoIngresado);
                DatosRetorno.putExtra("TIPO", TipoMovimiento);
                DatosRetorno.putExtra("CONCEPTO", TextoConcepto);
                setResult(RESULT_OK, DatosRetorno);
                finish();
            } else {
                EntradaMonto.setError("Ingrese un monto válido");
            }
        });
        ConfigurarInterfazDinamica();
    }

    private void ConfigurarInterfazDinamica() {
        String[] Categorias;

        if (TipoMovimiento.equals("AGREGAR")) {
            TextoTituloFormulario.setText("Ingresar Ahorro");
            BotonConfirmarMovimiento.setBackgroundColor(Color.parseColor("#1ABC9C"));
            Categorias = new String[]{"Sueldo", "Regalo", "Venta", "Ahorro", "Otros"};
        } else {
            TextoTituloFormulario.setText("Retirar Dinero Ahorrado");
            BotonConfirmarMovimiento.setBackgroundColor(Color.parseColor("#1ABC9C"));
            Categorias = new String[]{"Comida", "Transporte", "Servicios", "Diversión", "Otros"};
        }
    }
}