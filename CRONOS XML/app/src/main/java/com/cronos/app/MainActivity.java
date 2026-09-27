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
    }

    private void configurarMenuLateral() {
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_more);
        toolbar.setNavigationOnClickListener(view -> abrirMenu());

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
                cerrarSesion();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void abrirMenu() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void cargarFragment(Fragment fragment, String titulo) {
        toolbar.setTitle(titulo);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void cerrarSesion() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
