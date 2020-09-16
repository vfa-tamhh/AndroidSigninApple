package com.truyen24s.signinapple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nifcloud.mbaas.core.LoginCallback;
import com.nifcloud.mbaas.core.NCMB;
import com.nifcloud.mbaas.core.NCMBAppleParameters;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBUser;

public class TestActivity extends AppCompatActivity {
    AppleAuthenDialog appleAuthenDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        NCMB.initialize(getBaseContext(), "0e223936df11872251171e5a3c0c75a23e727f72b31fb9ee28fc22129df100cb", "291623c9dd66b53bafbb9fedd6cca92855604852ed25ebc320528adc08702d21");
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog();
            }
        });
    }
    private void callDialog() {
        String clientId = "com.firebaseapp.basicenglish-290e4";
        String redirectUrl = "https://dev.console.mbaas.api.nifcloud.com/2013-09-01/applications/HAKAaZWYtbn7CEmE/appleAuthenHandler";
        appleAuthenDialog = new AppleAuthenDialog(clientId, redirectUrl, this);
        appleAuthenDialog.setOnAppleAuthen(new AppleAuthenDialog.OnAppleAuthen() {
            @Override
            public void success(String userid, String accessToken, String clientId) {
                appleAuthenDialog.hide();
                NCMBAppleParameters parameters = new NCMBAppleParameters(userid, accessToken, clientId);
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
            }

            @Override
            public void failure(String errorMessage) {
                appleAuthenDialog.hide();
            }
        });
        appleAuthenDialog.show();
    }
}
