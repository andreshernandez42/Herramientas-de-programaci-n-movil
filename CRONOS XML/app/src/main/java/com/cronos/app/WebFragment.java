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
        // abre con la pagina de cronos
        cargarPagina();
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
        // para que la pagina se ajuste
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
    }

    private void configurarEventos() {
        btnWebGo.setOnClickListener(v -> cargarPagina());
        btnShowMap.setOnClickListener(v -> cargarMapa());
        btnShowWeb.setOnClickListener(v -> {
            etWebUrl.setText("https://cronostareas.com");
            cargarPagina();
        });
    }

    // mapa de la sede
    private void cargarMapa() {
        String mapUrl = "https://maps.google.com/maps?q=Politecnico+Grancolombiano+Bogota&z=15&output=embed";
        etWebUrl.setText("https://maps.google.com");
        // el mapa va dentro de un iframe
        String html = "<!DOCTYPE html><html><head><meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<style>html,body{margin:0;padding:0;height:100%;}iframe{border:0;width:100%;height:100%;}</style>"
                + "</head><body><iframe src='" + mapUrl + "' allowfullscreen loading='eager'></iframe></body></html>";
        webView.loadDataWithBaseURL("https://www.google.com", html, "text/html", "UTF-8", null);
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
                    + "<a class='btn-explore' href='#' onclick=\"document.getElementById('mas').style.display='block';this.style.display='none';return false;\">Explorar Contenido</a>"
                    + "<div id='mas' style='display:none;text-align:left;margin-top:16px'>"
                    + "<p>&#9989; Registra tareas con categoría, prioridad y fecha límite.</p>"
                    + "<p>&#127991; Organiza tus pendientes con etiquetas y filtros.</p>"
                    + "<p>&#9200; Programa recordatorios y limpia las tareas completadas.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body></html>";
            webView.loadDataWithBaseURL("https://cronostareas.com", previewHtml, "text/html", "UTF-8", null);
        } else {
            webView.loadUrl(url);
        }
    }
}
