package com.cronos.app;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VideoFragment extends Fragment {

    private static final String VIDEO_URL = "https://youtu.be/EU-A2QysKOQ";
    private Button btnVideoPlay;

    public VideoFragment() {
        super(R.layout.fragment_video);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnVideoPlay = view.findViewById(R.id.btnVideoPlay);
        btnVideoPlay.setOnClickListener(v -> reproducirVideo());
    }

    private void reproducirVideo() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(VIDEO_URL));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(requireContext(),
                    "No se encontró una aplicación para reproducir el video",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
