package com.example.spotifyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.spotifyapp.Connectors.SongService;
import com.example.spotifyapp.Model.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static androidx.browser.customtabs.CustomTabsIntent.KEY_DESCRIPTION;

public class HistoryActivity extends AppCompatActivity {
    TextView likedView;
    TextView dislikedView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences sharedPreferences;
    String likedSongIDs;
    private RequestQueue queue;
    private SongService songService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        likedSongIDs = "";
        likedView = findViewById(R.id.likedView);
        dislikedView = findViewById(R.id.DislikedView);
        songService = new SongService(getApplicationContext());

        sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        loadData();
    }

    private void loadData(){
        // docRef = db.collection("Notebook").document(id).collection("likedSongs");

        db.collection("Notebook").document(sharedPreferences.getString("userid", "No User")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String text = "";
                        String dislikedText = "";
                        if(documentSnapshot.exists()){
                            Map <String, String> map = new HashMap<>();
                            List<Map> liked = (List<Map>) documentSnapshot.get("likedSongs");
                            for(Map song : liked){
                                text += song.get("name")+ " - " + song.get("artist") + "\n";
                                likedSongIDs += song.get("id") + "%2C";
                            }
                            likedView.setText(text);

                            List<String> disliked = (List<String>) documentSnapshot.get("dislikedSongs");
                            for(String song : disliked){
                                dislikedText+= song + "\n";
                            }
                            dislikedView.setText((dislikedText));
                        } else {
                            Toast.makeText(HistoryActivity.this, "Document does not exist!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HistoryActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });


        //likedView.setText(text[0]);
    }

    public void saveSongs(View view){
        Toast.makeText(HistoryActivity.this, "Added", Toast.LENGTH_SHORT);
        songService.addSongsToLibrary(likedSongIDs);
    }
}