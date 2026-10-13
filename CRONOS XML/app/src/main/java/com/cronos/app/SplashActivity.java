package com.cronos.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 1200L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        animarLogo();
        iniciarAplicacion();
    }

    // animacion del reloj
    private void animarLogo() {
        findViewById(R.id.tvSplashLogo)
                .animate()
                .rotation(360f)
                .setDuration(SPLASH_DELAY)
                .start();
    }

    private void iniciarAplicacion() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // si ya inicio sesion va directo al home
            Class<?> destino = new SessionManager(this).haySesionActiva()
                    ? MainActivity.class : LoginActivity.class;
            startActivity(new Intent(SplashActivity.this, destino));
            finish();
        }, SPLASH_DELAY);
    }
}
