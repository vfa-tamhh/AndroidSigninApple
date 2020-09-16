package com.truyen24s.signinapple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class GoogleAppleSigninActivity extends AppCompatActivity {

    final String TAG = "JimmyHuynh";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_apple_signin);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.btn_clickme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OAuthProvider.Builder provider = OAuthProvider.newBuilder("apple.com");
                List<String> scopes =
                        new ArrayList<String>() {
                            {
                                add("email");
                                add("name");
                            }
                        };

                provider.setScopes(scopes);

                mAuth.startActivityForSignInWithProvider(GoogleAppleSigninActivity.this, provider.build())
                        .addOnSuccessListener(
                                new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        // Sign-in successful!
                                        Log.d(TAG, "activitySignIn:onSuccess:" + authResult.getUser());
                                        FirebaseUser user = authResult.getUser();
                                        OAuthCredential authCredential = (OAuthCredential)authResult.getCredential();
                                        AdditionalUserInfo additionalUserInfo = authResult.getAdditionalUserInfo();
//                                        additionalUserInfo.getProfile().get
                                        Log.d(TAG, "accessToken" + authCredential.getAccessToken());

                                        // ...
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "activitySignIn:onFailure", e);
                                    }
                                });
            }
        });
    }
}
