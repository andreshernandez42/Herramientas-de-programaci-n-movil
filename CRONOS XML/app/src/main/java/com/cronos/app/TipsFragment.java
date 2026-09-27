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
    }
}
