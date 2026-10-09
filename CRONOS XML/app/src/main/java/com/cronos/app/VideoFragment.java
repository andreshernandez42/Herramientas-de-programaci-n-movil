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
    private static final String SAMPLE_MP4 = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    private Button btnVideoPlay;
    private android.widget.VideoView sampleVideoView;

    public VideoFragment() {
        super(R.layout.fragment_video);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnVideoPlay = view.findViewById(R.id.btnVideoPlay);
        sampleVideoView = view.findViewById(R.id.sampleVideoView);

        btnVideoPlay.setOnClickListener(v -> reproducirVideo());
    }

    private void reproducirVideo() {
        if (sampleVideoView != null) {
            btnVideoPlay.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Iniciando video de muestra...", Toast.LENGTH_SHORT).show();

            android.widget.MediaController mediaController = new android.widget.MediaController(requireContext());
            mediaController.setAnchorView(sampleVideoView);
            sampleVideoView.setMediaController(mediaController);
            sampleVideoView.setVideoURI(Uri.parse(SAMPLE_MP4));

            sampleVideoView.setOnPreparedListener(mp -> sampleVideoView.start());

            sampleVideoView.setOnErrorListener((mp, what, extra) -> {
                btnVideoPlay.setVisibility(View.VISIBLE);
                abrirVideoExterno();
                return true;
            });
        } else {
            abrirVideoExterno();
        }
    }

    private void abrirVideoExterno() {
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
