package com.truyen24s.signinapple;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import java.util.UUID;

public class AppleAuthenDialog implements View.OnClickListener {
    private Context mContext;
    private OnAppleAuthen mOnAppleAuthen;
    private Dialog dialog;
    private WebView webview;
    private String clientId;
    private String redirectUrl;
    private String mUrl;

    public interface OnAppleAuthen {
        void success(String userid, String accessToken, String clientId);

        void failure(String errorMessage);
    }

    public AppleAuthenDialog(String clientId, String redirectUrl, Context context) {
        this.mContext = context;
        this.clientId = clientId;
        this.redirectUrl = redirectUrl;
        mOnAppleAuthen = null;
        buildDialog();
    }

    public void setOnAppleAuthen(OnAppleAuthen onAppleAuthen) {
        this.mOnAppleAuthen = onAppleAuthen;
    }

    private void buildDialog() {
        String state = UUID.randomUUID().toString();
        mUrl = "https://appleid.apple.com/auth/authorize?response_type=code%20id_token" +
                "&response_mode=form_post" +
                "&client_id=" + clientId +
                "&scope=name%20email" +
                "&state=" + state +
                "&redirect_uri=" + redirectUrl;

        dialog = new Dialog(mContext, R.style.AppTheme_FullScreenDialog);
        dialog.setContentView(R.layout.confirm_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);

        webview = dialog.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(webViewClient);
        webview.loadUrl(mUrl);

    }

    private WebViewClient webViewClient = new WebViewClient() {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return shouldOverrideUrlLoading(url);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
            Uri uri = request.getUrl();
            return shouldOverrideUrlLoading(uri.toString());
        }

        private boolean shouldOverrideUrlLoading(final String url) {
            String idToken = url.substring(url.lastIndexOf("id_token=") + 9, url.length());
            String code = url.substring(url.indexOf("code=") + 5, url.indexOf("id_token=") - 1);
            String payload = idToken.split("\\.")[1];//0 is header we ignore it for now
            String decoded = new String(java.util.Base64.getDecoder().decode(payload));
            Gson gson = new Gson();
            IdTokenPayload idTokenPayload = gson.fromJson(decoded, IdTokenPayload.class);
            String userId = idTokenPayload.getSub();

            if (idToken != null && code != null && userId != null) {
                mOnAppleAuthen.success(userId, code, clientId);
            } else {
                mOnAppleAuthen.failure("Error!");
            }
            return true; // Returning True means that application wants to leave the current WebView and handle the url itself, otherwise return false.
        }
    };

    public void show() {
        dialog.show();
    }
    public void hide() {
        dialog.hide();
    }

    @Override
    public void onClick(View v) {

    }
    private class IdTokenPayload {
        private String iss;
        private String aud;
        private Long exp;
        private Long iat;
        private String sub; //users unique id
        private String at_hash;
        private Long auth_time;

        public String getIss() {
            return iss;
        }

        public void setIss(String iss) {
            this.iss = iss;
        }

        public String getAud() {
            return aud;
        }

        public void setAud(String aud) {
            this.aud = aud;
        }

        public Long getExp() {
            return exp;
        }

        public void setExp(Long exp) {
            this.exp = exp;
        }

        public Long getIat() {
            return iat;
        }

        public void setIat(Long iat) {
            this.iat = iat;
        }

        public String getSub() {
            return sub;
        }

        public void setSub(String sub) {
            this.sub = sub;
        }

        public String getAt_hash() {
            return at_hash;
        }

        public void setAt_hash(String at_hash) {
            this.at_hash = at_hash;
        }

        public Long getAuth_time() {
            return auth_time;
        }

        public void setAuth_time(Long auth_time) {
            this.auth_time = auth_time;
        }

    }

}
