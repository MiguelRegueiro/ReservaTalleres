package com.example.reservatalleres;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etNombre, etCorreo, etTelefono;
    private Switch swNotificaciones;
    private CheckBox cbInformacion;
    private Button btnContinuar, btnLimpiar;

    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.scrollPrincipal), (v, insets) -> {
                    Insets barras = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    v.setPadding(barras.left, barras.top,
                            barras.right, barras.bottom);
                    return insets;
                });

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        swNotificaciones = findViewById(R.id.swNotificaciones);
        cbInformacion = findViewById(R.id.cbInformacion);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnLimpiar = findViewById(R.id.btnLimpiar);

        preferencias = getSharedPreferences(
                "ReservaTalleresPrefs", MODE_PRIVATE);

        cargarDatos();

        btnContinuar.setOnClickListener(v -> continuar());
        btnLimpiar.setOnClickListener(v -> limpiar());
    }

    private void cargarDatos() {
        etNombre.setText(preferencias.getString("nombre", ""));
        etCorreo.setText(preferencias.getString("correo", ""));
        etTelefono.setText(preferencias.getString("telefono", ""));

        swNotificaciones.setChecked(
                preferencias.getBoolean("notificaciones", false));

        cbInformacion.setChecked(
                preferencias.getBoolean("informacion", false));
    }

    private void continuar() {
        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("Introduce tu nombre");
            etNombre.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Introduce un correo válido");
            etCorreo.requestFocus();
            return;
        }

        if (telefono.isEmpty()) {
            etTelefono.setError("Introduce tu teléfono");
            etTelefono.requestFocus();
            return;
        }

        SharedPreferences.Editor editor = preferencias.edit();

        editor.putString("nombre", nombre);
        editor.putString("correo", correo);
        editor.putString("telefono", telefono);
        editor.putBoolean("notificaciones",
                swNotificaciones.isChecked());
        editor.putBoolean("informacion",
                cbInformacion.isChecked());

        editor.apply();

        Intent intent = new Intent(
                MainActivity.this, ReservaActivity.class);
        startActivity(intent);
    }

    private void limpiar() {
        etNombre.setText("");
        etCorreo.setText("");
        etTelefono.setText("");
        swNotificaciones.setChecked(false);
        cbInformacion.setChecked(false);

        SharedPreferences.Editor editor = preferencias.edit();

        editor.remove("nombre");
        editor.remove("correo");
        editor.remove("telefono");
        editor.remove("notificaciones");
        editor.remove("informacion");

        editor.apply();
    }
}
