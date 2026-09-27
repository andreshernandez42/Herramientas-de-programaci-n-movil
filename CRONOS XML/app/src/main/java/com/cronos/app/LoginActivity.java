package com.cronos.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button btnLogin;
    private Button btnGoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        vincularVistas();
        configurarEventos();
    }

    private void vincularVistas() {
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);
    }

    private void configurarEventos() {
        btnLogin.setOnClickListener(view -> validarInicioSesion());
        btnGoRegister.setOnClickListener(view -> abrirRegistro());
    }

    private void validarInicioSesion() {
        String correo = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(correo) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etLoginEmail.setError("Ingrese un correo válido");
            return;
        }
        abrirAplicacion();
    }

    private void abrirRegistro() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void abrirAplicacion() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("correo_usuario", etLoginEmail.getText().toString().trim());
        startActivity(intent);
        finish();
    }
}
