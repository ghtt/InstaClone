package com.akrasnoyarov.instaclone;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

public class AuthenticationDialog extends Dialog {
    private String requestUrl;
    private String instagramAppId;
    private String redirectUri;
    private String scope;
    private WebView webView;
    private final AuthListener mListener;

    public AuthenticationDialog(@NonNull Context context, AuthListener listener) {
        super(context);
        requestUrl = context.getString(R.string.auth_url);
        instagramAppId = context.getString(R.string.instagram_app_id);
        redirectUri = context.getString(R.string.redirect_uri);
        scope = context.getString(R.string.scope);
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_dialog);

        String request = requestUrl + "authorize" +
                "?client_id=" + instagramAppId +
                "&redirect_uri=" + redirectUri +
                "&scope=" + scope +
                "&response_type=code";

        Log.d("myLogs", "Auth dialog onCreate: " + request);
        webView = findViewById(R.id.web_view_auth);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(request);
        webView.setWebViewClient(webViewClient);

    }

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(redirectUri)) {
                if (url.contains("code=")) {
                    Uri uri = Uri.parse(url);


                    String access_token = uri.getQuery().substring(uri.getQuery().indexOf("=") + 1);
                    Log.d("myLogs", "Auth dialog access_token: " + access_token);
                    mListener.onOAuthTokenReceived(access_token);
                }
                AuthenticationDialog.this.dismiss();
                return true;
            }
            return false;
        }
    };
}
