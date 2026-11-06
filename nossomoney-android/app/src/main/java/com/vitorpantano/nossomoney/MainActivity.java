package com.vitorpantano.nossomoney;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private static final String START_URL = "https://vitorpantano.github.io/nossomoney/";
    private static final String HOST = "vitorpantano.github.io";

    private static final String ERROR_HTML =
            "<html><head><meta name='viewport' content='width=device-width, initial-scale=1'></head>"
            + "<body style='font-family:sans-serif;text-align:center;padding:48px 24px;color:#333'>"
            + "<h2>Sem conexão</h2>"
            + "<p>Não foi possível carregar o Nosso Money. Verifique sua internet.</p>"
            + "<p><a href='" + START_URL + "' style='font-size:18px'>Tentar novamente</a></p>"
            + "</body></html>";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);   // necessário para localStorage
        settings.setDatabaseEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (HOST.equals(uri.getHost())) {
                    return false; // continua dentro do app
                }
                // links externos abrem no navegador
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    view.loadDataWithBaseURL(null, ERROR_HTML, "text/html", "UTF-8", null);
                }
            }
        });

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            webView.loadUrl(START_URL);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
