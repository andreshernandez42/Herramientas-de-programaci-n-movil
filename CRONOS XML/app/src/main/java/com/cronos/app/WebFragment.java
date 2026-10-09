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
    private Button btnShowMap;
    private Button btnShowWeb;
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
        cargarMapa();
    }

    private void vincularVistas(View view) {
        etWebUrl = view.findViewById(R.id.etWebUrl);
        btnWebGo = view.findViewById(R.id.btnWebGo);
        btnShowMap = view.findViewById(R.id.btnShowMap);
        btnShowWeb = view.findViewById(R.id.btnShowWeb);
        webView = view.findViewById(R.id.webView);
    }

    private void configurarWebView() {
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
    }

    private void configurarEventos() {
        btnWebGo.setOnClickListener(v -> cargarPagina());
        btnShowMap.setOnClickListener(v -> cargarMapa());
        btnShowWeb.setOnClickListener(v -> cargarPagina());
    }

    private void cargarMapa() {
        etWebUrl.setText("https://maps.google.com");
        String mapHtml = "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'/>"
                + "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>"
                + "<style>html,body,#map{height:100%;margin:0;padding:0;font-family:sans-serif;}"
                + ".custom-popup{font-size:13px;color:#10305C;}"
                + "</style></head><body>"
                + "<div id='map'></div>"
                + "<script>"
                + "var map = L.map('map').setView([4.654, -74.058], 14);"
                + "L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {"
                + "  maxZoom: 19,"
                + "  attribution: '&copy; OpenStreetMap'"
                + "}).addTo(map);"
                + "L.marker([4.654, -74.057]).addTo(map).bindPopup('<b>🎓 Sede Poligran</b><br>Tareas académicas').openPopup();"
                + "L.marker([4.657, -74.062]).addTo(map).bindPopup('<b>💼 Oficina Trabajo</b><br>Entregables laborales');"
                + "L.marker([4.649, -74.059]).addTo(map).bindPopup('<b>🏠 Hogar / Casa</b><br>Actividades personales');"
                + "</script></body></html>";
        webView.loadDataWithBaseURL("https://openstreetmap.org", mapHtml, "text/html", "UTF-8", null);
    }

    private void cargarPagina() {
        String url = etWebUrl.getText().toString().trim();
        if (url.isEmpty()) {
            url = "https://cronostareas.com";
            etWebUrl.setText(url);
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        if (url.contains("cronostareas.com")) {
            String previewHtml = "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<style>"
                    + "body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background-color: #F7FBFC; margin: 0; padding: 24px; text-align: center; color: #10305C; }"
                    + ".header-tag { display: inline-block; background-color: #E0F7FA; color: #0077B6; font-size: 13px; font-weight: bold; padding: 6px 14px; border-radius: 16px; margin-bottom: 12px; }"
                    + ".card { background: #FFFFFF; border-radius: 16px; padding: 28px 20px; box-shadow: 0 4px 16px rgba(0,0,0,0.06); max-width: 480px; margin: 0 auto; }"
                    + "h1 { color: #10305C; font-size: 22px; margin: 0 0 10px 0; }"
                    + "p { color: #6B7280; font-size: 14px; line-height: 1.6; margin: 0 0 20px 0; }"
                    + ".btn-explore { background-color: #00B4D8; color: #FFFFFF; border: none; padding: 12px 28px; border-radius: 8px; font-size: 15px; font-weight: bold; cursor: pointer; text-decoration: none; display: inline-block; box-shadow: 0 2px 6px rgba(0,180,216,0.3); }"
                    + "</style></head><body>"
                    + "<div class='card'>"
                    + "<div class='header-tag'>🌐 Sitio Oficial</div>"
                    + "<h1>Bienvenido a Cronos Tareas</h1>"
                    + "<p>Plataforma para centralizar tu planificación personal, académica y laboral con recordatorios y priorización inteligente.</p>"
                    + "<a class='btn-explore' href='javascript:void(0)'>Explorar Contenido</a>"
                    + "</div>"
                    + "</body></html>";
            webView.loadDataWithBaseURL("https://cronostareas.com", previewHtml, "text/html", "UTF-8", null);
        } else {
            webView.loadUrl(url);
        }
    }
}
