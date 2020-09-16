package com.truyen24s.signinapple;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.nifcloud.mbaas.core.LoginCallback;
import com.nifcloud.mbaas.core.NCMB;
import com.nifcloud.mbaas.core.NCMBAppleParameters;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBUser;


import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NCMB.initialize(getBaseContext(), "0e223936df11872251171e5a3c0c75a23e727f72b31fb9ee28fc22129df100cb", "291623c9dd66b53bafbb9fedd6cca92855604852ed25ebc320528adc08702d21");
        findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWebview();
            }
        });
        webView = findViewById(R.id.webview);
    }

    private void callWebview() {
        String state = UUID.randomUUID().toString();
        String urli = "https://appleid.apple.com/auth/authorize?response_type=code%20id_token" +
                "&response_mode=form_post" +
                "&client_id=com.firebaseapp.basicenglish-290e4" +
                "&scope=name%20email" +
                "&state=" + state +
                "&redirect_uri=https://dev.console.mbaas.api.nifcloud.com/2013-09-01/applications/HAKAaZWYtbn7CEmE/appleAuthenHandler";

//        setupAppleWebviewDialog(urli);
//        WebView webView =new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
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
                Log.i("JimmyHuynh", "shouldOverrideUrlLoading() URL : " + url);
                String idToken = url.substring(url.lastIndexOf("id_token=") + 9, url.length());
                String code = url.substring(url.indexOf("code=") + 5, url.indexOf("id_token=") - 1);
                Log.i("JimmyHuynh", "id_token: " + idToken);
                Log.i("JimmyHuynh", "code: " + code);
                // Signin Nifcloud here...
                String payload = idToken.split("\\.")[1];//0 is header we ignore it for now
                String decoded = new String(java.util.Base64.getDecoder().decode(payload));
                Gson gson = new Gson();
                IdTokenPayload idTokenPayload = gson.fromJson(decoded, IdTokenPayload.class);
                String userId = idTokenPayload.getSub();
                // Setting value for apple parameters
                NCMBAppleParameters parameters = new NCMBAppleParameters(userId, code, "com.firebaseapp.basicenglish-290e4");
                NCMBUser.loginInBackgroundWith(parameters, new LoginCallback() {
                    @Override
                    public void done(NCMBUser ncmbUser, NCMBException e) {
                        if (e != null) {
                            Log.i("JimmyHuynh", "Error: " + e.getMessage());
                        } else {
                            Log.i("JimmyHuynh", "Registration the user is OK: " + ncmbUser.getObjectId());
                        }
                    }
                });
                // Here put your code

                return true; // Returning True means that application wants to leave the current WebView and handle the url itself, otherwise return false.
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Do something on page loading started
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("JimmyHuynh", url);
            }

        });
        webView.loadUrl(urli);


    }

    private void setupAppleWebviewDialog(String url) {
        Dialog appledialog = new Dialog(this);
        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                view.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Do something on page loading started
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
        webView.loadUrl(url);
        appledialog.setContentView(webView);
        appledialog.show();
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
