package com.example.spotifyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifyapp.Connectors.PlaylistService;
import com.example.spotifyapp.Model.Playlist;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocalPlaylist extends AppCompatActivity {
    private Button locationButton;
    private TextView locationText;
    private TextView playlistText;
    private String city = "";

    private PlaylistService playlistService;
    private ArrayList<Playlist> localPlaylist;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_playlist);

        playlistService =  new PlaylistService(getApplicationContext());
        locationText = findViewById(R.id.locationText);
        locationButton = findViewById(R.id.getLocation);
        playlistText = findViewById(R.id.playlistDisplay);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
    public void setLocationButton(View v){
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        if(ActivityCompat.checkSelfPermission(LocalPlaylist.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getLocation();
        }else{
            ActivityCompat.requestPermissions(LocalPlaylist.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            getLocation();
        }
        Log.d("City", city);
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
                            String text = "City: " + addresses.get(0).getLocality() + "\n"
                                    + "Country: " + addresses.get(0).getCountryName();
                            locationText.setText(text);
                            String locality = addresses.get(0).getLocality();
                            for(int i = 0; i < locality.length(); i++){
                                if(locality.charAt(i) == ' '){
                                    city += "+";
                                }else{
                                    city += locality.charAt(i);
                                }
                            }
                            getLocalPlaylists();
                            Toast.makeText(LocalPlaylist.this, "Called", Toast.LENGTH_LONG).show();
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
        playlistService.getLocalPlaylist(city, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                localPlaylist = playlistService.getPlaylists();
                LocalPlaylist.this.updatePlaylists();
            }
        });
    }
    private void updatePlaylists() {
        if (localPlaylist.size() > 0) {
            String x = "";
            for(int i = 0; i < 5; i++){
                x += localPlaylist.get(i).getName();
                x += "\n";
            }
            playlistText.setText(x);
        }
    }
}