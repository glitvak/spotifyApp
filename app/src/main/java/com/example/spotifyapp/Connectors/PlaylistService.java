package com.example.spotifyapp.Connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.spotifyapp.Model.Playlist;
import com.example.spotifyapp.Model.Song;
import com.example.spotifyapp.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaylistService {
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    public PlaylistService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public ArrayList<Playlist> getLocalPlaylist(String city, final VolleyCallBack callBack){
        final int limit = 10;
        String endpoint = "https://api.spotify.com/v1/search?q=" + city + "&type=playlist&limit=" + limit + "";
        Log.d("Endpoint", endpoint);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET,endpoint,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject = response.optJSONObject("playlists");
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            for(int i = 0; i < limit; i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                Playlist playlist = new Playlist();
                                playlist.setName(jsonObject1.getString("name"));
                                playlists.add(playlist);
                            }
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
        return playlists;
    }
}
