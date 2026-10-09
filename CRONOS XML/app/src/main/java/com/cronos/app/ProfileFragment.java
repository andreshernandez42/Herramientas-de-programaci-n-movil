package com.cronos.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private Button btnEditProfile;
    private Button btnSafeExit;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnSafeExit = view.findViewById(R.id.btnSafeExit);

        btnEditProfile.setOnClickListener(v -> editarPerfil());
        btnSafeExit.setOnClickListener(v -> confirmarSalidaSegura());
    }

    private void editarPerfil() {
        Toast.makeText(requireContext(),
                "Edición de perfil habilitada para siguiente iteración",
                Toast.LENGTH_SHORT).show();
    }

    private void confirmarSalidaSegura() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Salida segura")
                .setMessage("¿Desea cerrar la sesión de forma segura y proteger sus datos?")
                .setPositiveButton("Salir", (dialog, which) -> {
                    Toast.makeText(requireContext(), "Sesión cerrada de forma segura", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
