package com.cronos.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;

public class TipsFragment extends Fragment {

    private MaterialCardView cardTip1;
    private MaterialCardView cardTip2;
    private MaterialCardView cardTip3;

    public TipsFragment() {
        super(R.layout.fragment_tips);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardTip1 = view.findViewById(R.id.cardTip1);
        cardTip2 = view.findViewById(R.id.cardTip2);
        cardTip3 = view.findViewById(R.id.cardTip3);

        cardTip1.setOnClickListener(v -> mostrarDetalleTip("Organización por Categorías"));
        cardTip2.setOnClickListener(v -> mostrarDetalleTip("Priorización Inteligente"));
        cardTip3.setOnClickListener(v -> mostrarDetalleTip("Descansos Activos"));
    }

    private void mostrarDetalleTip(String titulo) {
        Toast.makeText(requireContext(), titulo, Toast.LENGTH_SHORT).show();

        String detalle;
        if (titulo.contains("Categorías")) {
            detalle = "Mantén agrupadas tus responsabilidades en Universidad, Trabajo, Casa, Salud, Proyectos y Personal. Esto reduce la fatiga mental y permite enfocarte en un contexto a la vez.";
        } else if (titulo.contains("Priorización")) {
            detalle = "Inicia cada jornada resolviendo primero las tareas de prioridad Alta. Esto asegura avanzar en lo crucial antes de atender imprevistos de menor impacto.";
        } else {
            detalle = "Aplica la técnica Pomodoro: bloques de 25 minutos de concentración total y 5 minutos de descanso activo para sostener un alto rendimiento sin agotamiento.";
        }

        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("💡 " + titulo)
                .setMessage(detalle)
                .setPositiveButton("Entendido", null)
                .show();
    }
}
