package com.example.spotifyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifyapp.Connectors.PlaylistService;
import com.example.spotifyapp.Model.Playlist;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocalPlaylist extends AppCompatActivity {
    private Button locationButton;
    private TextView playlistText;
    private ImageView image1, image2, image3, image4;
    private String city = "";
    private int limit = 7;
    private GradientDrawable backgroundGradient;

    private PlaylistService playlistService;
    private ArrayList<Playlist> localPlaylist;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onResume() {
        super.onResume();
        backgroundGradient = (GradientDrawable)image1.getBackground();
        backgroundGradient.setStroke(0, Color.WHITE);
        backgroundGradient = (GradientDrawable)image2.getBackground();
        backgroundGradient.setStroke(0, Color.WHITE);
        backgroundGradient = (GradientDrawable)image3.getBackground();
        backgroundGradient.setStroke(0, Color.WHITE);
        backgroundGradient = (GradientDrawable)image4.getBackground();
        backgroundGradient.setStroke(0, Color.WHITE);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_playlist);

        playlistService =  new PlaylistService(getApplicationContext());
        locationButton = findViewById(R.id.getLocation);
        playlistText = findViewById(R.id.playlistDisplay);
        image1 = findViewById(R.id.playlistArt1);
        image2 = findViewById(R.id.playlistArt2);
        image3 = findViewById(R.id.playlistArt3);
        image4 = findViewById(R.id.playlistArt4);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
    public void setLocationButton(View v){
        if(ActivityCompat.checkSelfPermission(LocalPlaylist.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            getLocation();
        }else{
            ActivityCompat.requestPermissions(LocalPlaylist.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            getLocation();
        }
    }
    private void getLocation(){
        if(ActivityCompat.checkSelfPermission(LocalPlaylist.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null){
                        try {
                            Geocoder geocoder = new Geocoder(LocalPlaylist.this,
                                    Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1);
                            playlistText.setText("Popular playlists in: " +  addresses.get(0).getLocality());
                            String locality = addresses.get(0).getLocality();
                            for(int i = 0; i < locality.length(); i++){
                                if(locality.charAt(i) == ' '){
                                    city += "+";
                                }else{
                                    city += locality.charAt(i);
                                }
                            }
                            getLocalPlaylists();
                        }
                        catch (IOException e){
                            Toast.makeText(LocalPlaylist.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }
    private void getLocalPlaylists() {
        playlistService.getLocalPlaylist(city, limit, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                localPlaylist = playlistService.getPlaylists();
                LocalPlaylist.this.updatePlaylists();
            }
        });
    }
    private void updatePlaylists() {
        if (localPlaylist.size() > 0) {
//            String x = "";
//            int iter = 0;
//            for(int i = 0; i < limit; i++){
//                x += localPlaylist.get(i).getName();
//                x += "\n";
//                SpannableString ss = new SpannableString(x);
//                String link = localPlaylist.get(i).getLink();
//                ClickableSpan clickableSpan = new ClickableSpan() {
//                    @Override
//                    public void onClick(@NonNull View widget) {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
//                        startActivity(browserIntent);
//                    }
//                };
//                ss.setSpan(clickableSpan, iter, x.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                playlistText.append(ss.subSequence(iter, x.length()));
//                iter = x.length();
//            }
//            playlistText.setMovementMethod(LinkMovementMethod.getInstance());
            int width = 15;
            Picasso.get().load(localPlaylist.get(0).getURL()).into(image1);
            backgroundGradient = (GradientDrawable)image1.getBackground();
            backgroundGradient.setStroke(width, Color.WHITE);
            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(localPlaylist.get(0) != null) {
                        Log.d("URL", localPlaylist.get(0).getURL());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(localPlaylist.get(0).getLink()));
                        startActivity(browserIntent);
                    }
                }
            });
            Picasso.get().load(localPlaylist.get(1).getURL()).into(image2);
            backgroundGradient = (GradientDrawable)image2.getBackground();
            backgroundGradient.setStroke(width, Color.WHITE);
            image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(localPlaylist.get(1) != null) {
                        Log.d("URL", localPlaylist.get(1).getURL());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(localPlaylist.get(1).getLink()));
                        startActivity(browserIntent);
                    }
                }
            });
            Picasso.get().load(localPlaylist.get(2).getURL()).into(image3);
            backgroundGradient = (GradientDrawable)image3.getBackground();
            backgroundGradient.setStroke(width, Color.WHITE);
            image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(localPlaylist.get(2) != null) {
                        Log.d("URL", localPlaylist.get(2).getURL());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(localPlaylist.get(2).getLink()));
                        startActivity(browserIntent);
                    }
                }
            });
            Picasso.get().load(localPlaylist.get(3).getURL()).into(image4);
            backgroundGradient = (GradientDrawable)image4.getBackground();
            backgroundGradient.setStroke(width, Color.WHITE);
            image4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(localPlaylist.get(3) != null) {
                        Log.d("URL", localPlaylist.get(3).getURL());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(localPlaylist.get(3).getLink()));
                        startActivity(browserIntent);
                    }
                }
            });
        }
    }
}