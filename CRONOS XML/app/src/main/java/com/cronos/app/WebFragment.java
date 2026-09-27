package com.cronos.app;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WebFragment extends Fragment {

    private EditText etWebUrl;
    private Button btnWebGo;
    private WebView webView;

    public WebFragment() {
        super(R.layout.fragment_web);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vincularVistas(view);
        configurarWebView();
        configurarEventos();
        cargarPagina();
    }

    private void vincularVistas(View view) {
        etWebUrl = view.findViewById(R.id.etWebUrl);
        btnWebGo = view.findViewById(R.id.btnWebGo);
        webView = view.findViewById(R.id.webView);
    }

    private void configurarWebView() {
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private void configurarEventos() {
        btnWebGo.setOnClickListener(v -> cargarPagina());
    }

    private void cargarPagina() {
        String url = etWebUrl.getText().toString().trim();
        if (url.isEmpty()) {
            url = "https://example.com";
            etWebUrl.setText(url);
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        webView.loadUrl(url);
    }
}
