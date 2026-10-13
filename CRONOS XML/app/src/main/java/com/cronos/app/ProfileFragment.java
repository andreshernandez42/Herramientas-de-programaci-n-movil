package com.cronos.app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private TextView tvStatTotal;
    private TextView tvStatCompleted;
    private TextView tvStatPending;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vincularVistas(view);
        mostrarDatosUsuario();
    }

    private void vincularVistas(View view) {
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvStatTotal = view.findViewById(R.id.tvStatTotal);
        tvStatCompleted = view.findViewById(R.id.tvStatCompleted);
        tvStatPending = view.findViewById(R.id.tvStatPending);
    }

    // pone el nombre y cuantas tareas tiene
    private void mostrarDatosUsuario() {
        SessionManager session = new SessionManager(requireContext());
        tvProfileName.setText(session.getNombreActivo());
        tvProfileEmail.setText(session.getCorreoActivo());

        List<TaskItem> tareas = new TaskRepository(requireContext()).obtenerTareas();
        int completadas = TaskRepository.contarCompletadas(tareas);
        tvStatTotal.setText(String.valueOf(tareas.size()));
        tvStatCompleted.setText(String.valueOf(completadas));
        tvStatPending.setText(String.valueOf(tareas.size() - completadas));
    }
}
