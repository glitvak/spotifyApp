package com.example.spotifyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.spotifyapp.Connectors.SongService;
import com.example.spotifyapp.Model.Artist;
import com.example.spotifyapp.Model.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.type.DateTime;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class RecActivity extends AppCompatActivity {

    private SongService songService;
    private ArrayList<Song> recentlyPlayedTracks;
    private ArrayList<Artist> topArtists;
    private JSONObject analysis;
    private TextView analysisView;
    private ImageView img;
    private Song rec;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Timestamp date;
    public final static long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec);


        songService = new SongService(getApplicationContext());

        Button recBtn = findViewById(R.id.rec);
        Button like = findViewById(R.id.likeBtn);
        Button dislike = findViewById(R.id.DislikeBtn);
        Button history = findViewById(R.id.historyBtn);
        Button local = findViewById(R.id.localButton);
        img = findViewById(R.id.albumArt);
        analysisView = findViewById(R.id.analysis);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        checkDB(sharedPreferences.getString("userid", "No User"));

        //userView.setText();

        recBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSongs();
                like.setEnabled(true);
                dislike.setEnabled(true);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeSong(v, sharedPreferences.getString("userid", "No User"));
                like.setEnabled(false);
                dislike.setEnabled(false);
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dislikeSong(v, sharedPreferences.getString("userid", "No User"));
                like.setEnabled(false);
                dislike.setEnabled(false);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("URL", rec.getUrl());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rec.getUrl()));
                startActivity(browserIntent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent newintent = new Intent(RecActivity.this, HistoryActivity.class);
                Intent newintent = new Intent(RecActivity.this, HistoryTabActivity.class);
                startActivity(newintent);
            }
        });

        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newintent = new Intent(RecActivity.this, LocalPlaylist.class);
                startActivity(newintent);
            }
        });
    }

    private void getAnalysis(){
        Song current = recentlyPlayedTracks.get(0);
        //Log.d("ERR", current.getId());
        songService.getSongInfo(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                analysis = songService.getAnalysis();
                //updateSong();
            }
        }, current.getId());
    }

    private void getSongs(){
        songService.getRecentlyPlayedTracks(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                recentlyPlayedTracks = songService.getSongs();
                getArtists();
            }
        });
    }

    private void updateSong(){
        String text = "";
        if(analysis == null){
            text = "";
        }
        else {
            text = analysis.toString();
            analysisView.setText(text);
        }
    }

    private void getArtists() {
        songService.getTopArtists(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                topArtists = songService.getArtists();
                getRec();
//                String text;
//                if(topArtists.size() > 0){
//                    text = "";
//                    for(int i = 0; i < 5; i++){
//                        text += topArtists.get(i).getName();
//                        text += "\n";
//                    }
//                    analysisView.setText(text);
//                }
            }
        });
    }

    private void getRec(){
        if(recentlyPlayedTracks.size() > 0 && topArtists.size() > 0) {
            songService.getRecommendation(new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    rec = songService.getRec();
                    String text = "";
                    if (rec != null) {
                        text += rec.getArtist() + " - " + rec.getName();
                        Log.d("ID: ", rec.getId());
                    }

                    loadImage(rec.getId());
                    analysisView.setText(text);
                }
            }, recentlyPlayedTracks, topArtists);
        }
    }

    private void likeSong(View v, String id){
        if(rec != null){
            DocumentReference user_doc = db.collection("Notebook").document(id);

            Map<String, String> likedSongs = new HashMap<>();
            likedSongs.put("artist", rec.getArtist());
            likedSongs.put("name", rec.getName());
            likedSongs.put("id", rec.getId());
            user_doc.update("likedSongs", FieldValue.arrayUnion(likedSongs))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);

                        }
                    });



        }
    }

    private void dislikeSong(View v, String id){
        if(rec != null){
            DocumentReference user_doc = db.collection("Notebook").document(id);

            user_doc.update("dislikedSongs", FieldValue.arrayUnion(rec.getArtist() + " - " + rec.getName()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });
        }
    }

    private void checkDB(String id){
        DocumentReference doc = db.collection("Notebook").document(id);

        doc.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            ;
                        } else {
//                            Toast.makeText(MainActivity.this, "Document does not exist!", Toast.LENGTH_SHORT).show();
                            Map<String, Object> docData = new HashMap<>();
                            docData.put("dislikedSongs", Arrays.asList());
                            docData.put("likedSongs", Arrays.asList());
                            docData.put("newRecommendation", new Timestamp(new Date()));
                            docData.put("userID", id);

                            doc.set(docData, SetOptions.merge());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", e.toString());
                    }
                });
    }

//    private Timestamp getDate(String id){
//        final Timestamp[] currDate = new Timestamp[1];
//        db.collection("Notebook").document(id).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if(documentSnapshot.exists()){
//                            currDate[0] = (Timestamp) documentSnapshot.get("newRecommendation");
//
//                        } else {
//                            Toast.makeText(RecActivity.this, "Document does not exist!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(RecActivity.this, "Error!", Toast.LENGTH_SHORT).show();
//                        Log.d("TAG", e.toString());
//                    }
//                });
//        return currDate[0];
//    }

    public void loadImage(String id){
        songService.getArt(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                String url = songService.getUrl();

                Picasso.get().load(url).into(img);
            }
        }, id);
    }
}