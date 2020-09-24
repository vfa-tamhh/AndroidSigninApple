package com.example.signinwithapple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nifcloud.mbaas.core.LoginCallback;
import com.nifcloud.mbaas.core.NCMB;
import com.nifcloud.mbaas.core.NCMBAppleOauthDialog;
import com.nifcloud.mbaas.core.NCMBAppleParameters;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NCMB.initialize(this, "62b9674928b236e1476b5b471c9de897c26b923c04809dc4728e2a9253b30928", "f6bdd696b85d96849f63f128acee08db905984bb2f9a1684834e0aca2c401433");
        findViewById(R.id.btn_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinApple();
            }
        });
    }
    private void signinApple() {
        String clientId = "com.firebaseapp.basicenglish-290e4";
        String redirectUrl = "https://dev.mbaas.api.nifcloud.com/2013-09-01/applications/RowMFMpEmWtoGBJE/appleAuthenHandler";
        NCMBAppleOauthDialog ncmbAppleOauthDialog = new NCMBAppleOauthDialog(clientId, redirectUrl, this);
        ncmbAppleOauthDialog.setOnAppleAuthen(new NCMBAppleOauthDialog.OnAppleAuthen() {
            @Override
            public void success(String s, String s1, String s2) {
                NCMBAppleParameters appleParameters = new NCMBAppleParameters(s, s1, s2);
                NCMBUser.loginInBackgroundWith(appleParameters, new LoginCallback() {
                    @Override
                    public void done(NCMBUser ncmbUser, NCMBException e) {
                        if (e != null) {
                            // handler on error
                            Log.d("JimmyHuynh", e.getMessage());
                        } else {
                            // handler if login successful
                            Log.d("JimmyHuynh", "Login is successful!");

                        }
                    }
                });
            }

            @Override
            public void failure(String s) {
                Log.d("JimmyHuynh", s);
            }
        });
    }
}