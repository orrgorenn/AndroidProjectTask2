package il.co.hit.android;

import static il.co.hit.android.Config.MAIN_EXTRA_USERNAME;
import static il.co.hit.android.Config.PROFILE_WEBSITE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends AppCompatActivity {
    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        webView = findViewById(R.id.webview_main);
        webView.setWebViewClient(new WebViewClient());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString(PROFILE_WEBSITE_URL);
        }

        goToWebsite();
    }

    private void goToWebsite() {
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        webView.destroy();
    }
}