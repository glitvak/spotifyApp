package com.example.spotifyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.spotifyapp.Connectors.UserService;
import com.example.spotifyapp.Model.User;
import com.example.spotifyapp.db.AppDatabase;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import io.perfmark.Tag;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;
    private AppDatabase database;

    private RequestQueue queue;

    private static final String CLIENT_ID = "f5c5ad08becf43cab0497728ee22e142";
    private static final String REDIRECT_URI = "com.testingapi://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private,playlist-read-private,user-top-read,playlist-modify-public,playlist-modify-private";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        Button login = findViewById(R.id.loginButton);

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
    }
    public void startAuthentication(View view) {
        authenticateSpotify();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.apply();
                    waitForUserInfo();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Toast.makeText(SplashActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    break;

                // Most likely auth flow was cancelled
                default:
                    Toast.makeText(SplashActivity.this, "Default", Toast.LENGTH_SHORT).show();
                    // Handle other cases
            }
        }
    }
    private void authenticateSpotify() {
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthorizationRequest request = builder.build();
        //AuthorizationClient.openLoginInBrowser(this,request);
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }
    private void waitForUserInfo() {
        final UserService userService = new UserService(queue, msharedPreferences);
        userService.get(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                User user = userService.getUser();
                editor = SplashActivity.this.getSharedPreferences("SPOTIFY", 0).edit();
                database = AppDatabase.getInstance(SplashActivity.this);
                if(database.userDao().getOne(user.id) == null){
                    com.example.spotifyapp.db.User newUser = new com.example.spotifyapp.db.User();
                    newUser.userid = user.id;
                    database.userDao().insertAll(newUser);
                }
                //editor.putString("userid", user.id);
                Log.d("STARTING", "GOT USER INFORMATION");
                // We use commit instead of apply because we need the information stored immediately
                editor.commit();
                SplashActivity.this.startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        //Intent newintent = new Intent(SplashActivity.this, DisplayData.class);
        Intent newintent = new Intent(SplashActivity.this, RecActivity.class);
        startActivity(newintent);
    }
}