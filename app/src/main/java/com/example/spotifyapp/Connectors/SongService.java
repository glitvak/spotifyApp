package com.example.spotifyapp.Connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotifyapp.HistoryActivity;
import com.example.spotifyapp.Model.Artist;
import com.example.spotifyapp.Model.Song;
import com.example.spotifyapp.Model.User;
import com.example.spotifyapp.VolleyCallBack;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SongService {
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Artist> artists = new ArrayList<>();
    private JSONObject[] a = new JSONObject[1];
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Song recSong;
    private String url;

    public SongService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public JSONObject getAnalysis(){ return a[0];}

    public ArrayList<Artist> getArtists(){ return artists;}

    public Song getRec(){ return recSong;}

    public String getUrl(){ return url;}

    public ArrayList<Song> getRecentlyPlayedTracks(final VolleyCallBack callBack){
        String endpoint = "https://api.spotify.com/v1/me/player/recently-played";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET,endpoint,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        JSONArray jsonArray = response.optJSONArray("items");
                        for (int n = 0; n < jsonArray.length(); n++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(n);
                                object = object.optJSONObject("track");
                                Log.d("JSONSong",object.toString(4));
                                Log.d("JSONurl", object.getJSONObject("external_urls").getString("spotify"));
                                Song song = gson.fromJson(object.toString(), Song.class);
                                song.setUrl(object.getJSONObject("external_urls").getString("spotify"));
                                songs.add(song);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callBack.onSuccess();
                    }
                }, null)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }

    public JSONObject getSongInfo(final VolleyCallBack callBack, String id){

        String endpoint = "https://api.spotify.com/v1/audio-features/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //JSONObject jsonArray = response.optJSONObject("");
                JSONObject object = response;
                a[0] = object;
                callBack.onSuccess();
            }
        }, null)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return a[0];
    }
    public void addSongsToLibrary(String likedSongIDs) {
        String endpoint = "https://api.spotify.com/v1/me/tracks?ids=" + likedSongIDs;
        Log.d("Liked Song String:", likedSongIDs);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, endpoint,null, response -> {
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
    public ArrayList<Artist> getTopArtists(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/top/artists";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET,endpoint,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        JSONArray jsonArray = response.optJSONArray("items");
                        for (int n = 0; n < jsonArray.length(); n++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(n);
                                //object = object.optJSONObject("track");
                                Artist artist = gson.fromJson(object.toString(), Artist.class);
                                JSONArray genre = object.optJSONArray("genres");
                                artist.setGenre(genre.get(0).toString().replaceAll(" ", "%20").replaceAll("&", "%26"));
                                artists.add(artist);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        callBack.onSuccess();
                    }
                }, null)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return artists;
    }

    public Song getRecommendation(final VolleyCallBack callBack, ArrayList<Song> songs, ArrayList<Artist> artists){
        String endpoint = "https://api.spotify.com/v1/recommendations?market=US";
        endpoint+= "&seed_artists=";
        for(int i = 0; i < 2; i++){
            int rand = (int) (Math.random() * (5 - 0 + 1) + 0);
            if(i == 1){
                endpoint += artists.get(rand).getId();
            }
            else {
                endpoint += artists.get(rand).getId();
                endpoint+= "%2C";
            }
        }
        endpoint+= "&seed_genres=";
        for(int i = 0; i < 2; i++){
            int rand = (int) (Math.random() * (5 - 0 + 1) + 0);
            if(i == 1){
                endpoint += artists.get(rand).getGenre();
            }
            else {
                endpoint += artists.get(rand).getGenre();
                endpoint+= "%2C";
            }
        }
        endpoint+= "&seed_tracks=";
        for(int i = 0; i < 1; i++){
            int rand = (int) (Math.random() * (5 - 0 + 1) + 0);
            if(i == 0){
                endpoint += songs.get(rand).getId();
            }
            else {
                endpoint += songs.get(rand).getId();
                endpoint+= "%2C";
            }
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET,endpoint,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        JSONArray jsonArray = response.optJSONArray("tracks");
                        try {
                            JSONObject object = jsonArray.getJSONObject(0);
                            recSong = gson.fromJson(object.toString(), Song.class);
                            recSong.setUrl(jsonArray.getJSONObject(0).getString("preview_url"));
                            String artist = object.optJSONArray("artists").getJSONObject(0).getString("name");
                            recSong.setArtist(artist);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        callBack.onSuccess();
                    }
                }, null)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return recSong;
    }

    public String getArt(final VolleyCallBack callBack, String id){
        String endpoint = "https://api.spotify.com/v1/tracks/" + id;
        //final String[] url = {""};
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONObject("album").optJSONArray("images");
//                JSONObject object = response;
//                a[0] = object;
                Log.d("TEST", jsonArray.toString());
                try {
                    url = jsonArray.getJSONObject(1).get("url").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.onSuccess();
            }
        }, null)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);

        return url;
    }
}
