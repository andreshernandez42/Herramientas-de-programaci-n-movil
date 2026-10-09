package com.cronos.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private android.widget.ImageButton btnMenuToggle;
    private android.widget.TextView tvToolbarTitle;
    private android.widget.Button btnToolbarSafeExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vincularVistas();
        configurarMenuLateral();

        if (savedInstanceState == null) {
            cargarFragment(new HomeFragment(), "Mis tareas");
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void vincularVistas() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        btnMenuToggle = findViewById(R.id.btnMenuToggle);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        btnToolbarSafeExit = findViewById(R.id.btnToolbarSafeExit);
    }

    private void configurarMenuLateral() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (btnMenuToggle != null) {
            btnMenuToggle.setOnClickListener(view -> abrirMenu());
        }
        if (btnToolbarSafeExit != null) {
            btnToolbarSafeExit.setOnClickListener(view -> confirmarSalidaSegura());
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                cargarFragment(new HomeFragment(), "Mis tareas");
            } else if (id == R.id.nav_profile) {
                cargarFragment(new ProfileFragment(), "Perfil");
            } else if (id == R.id.nav_gallery) {
                cargarFragment(new GalleryFragment(), "Fotos");
            } else if (id == R.id.nav_video) {
                cargarFragment(new VideoFragment(), "Video");
            } else if (id == R.id.nav_web) {
                cargarFragment(new WebFragment(), "Web");
            } else if (id == R.id.nav_controls) {
                cargarFragment(new ControlsFragment(), "Botones / Controles");
            } else if (id == R.id.nav_tips) {
                cargarFragment(new TipsFragment(), "Tips");
            } else if (id == R.id.nav_logout) {
                confirmarSalidaSegura();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    public void abrirMenu() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void cargarFragment(Fragment fragment, String titulo) {
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText(titulo);
        }
        toolbar.setTitle(titulo);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    public void confirmarSalidaSegura() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("🔒 Salida segura")
                .setMessage("¿Desea cerrar la sesión de forma segura y proteger sus datos?")
                .setPositiveButton("Salir", (dialog, which) -> cerrarSesion())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cerrarSesion() {
        android.widget.Toast.makeText(this, "Sesión cerrada de forma segura", android.widget.Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
