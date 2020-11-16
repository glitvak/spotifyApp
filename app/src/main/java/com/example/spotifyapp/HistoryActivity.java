package com.example.spotifyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static androidx.browser.customtabs.CustomTabsIntent.KEY_DESCRIPTION;

public class HistoryActivity extends AppCompatActivity {
    TextView likedView;
    TextView dislikedView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        likedView = findViewById(R.id.likedView);
        dislikedView = findViewById(R.id.DislikedView);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        loadData(sharedPreferences.getString("userid", "No User"));
    }

    private void loadData(String id){
        // docRef = db.collection("Notebook").document(id).collection("likedSongs");

        db.collection("Notebook").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String text = "";
                        String dislikedText = "";
                        if(documentSnapshot.exists()){
                            List<String> liked = (List<String>) documentSnapshot.get("likedSongs");
                            for(String song : liked){
                                text += song+"\n";
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
}