package com.example.spotifyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.spotifyapp.Connectors.SongService;
import com.example.spotifyapp.Model.Song;

import java.util.ArrayList;

public class DisplayData extends AppCompatActivity {
    private TextView userView;
    private TextView songView;
    private Button addBtn;
    private Song song;
    private String url = "http://spotify.com/";

    private SongService songService;
    private ArrayList<Song> recentlyPlayedTracks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        songService = new SongService(getApplicationContext());
        userView = (TextView) findViewById(R.id.user);
        songView = (TextView) findViewById(R.id.song);
        addBtn = (Button) findViewById(R.id.add);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));

        getTracks();
    }
    private void getTracks() {
        songService.getRecentlyPlayedTracks(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                recentlyPlayedTracks = songService.getSongs();
                DisplayData.this.updateSong();
            }
        });
    }

    private void updateSong() {
        if (recentlyPlayedTracks.size() > 0) {
            String x = "";
            for(int i = 0; i < 5; i++){
                x += recentlyPlayedTracks.get(i).getName();
                x += "\n";
            }
            songView.setText(x);
            song = recentlyPlayedTracks.get(0);
        }
    }
    public void goToSu (View view) {
        goToUrl (url);
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}