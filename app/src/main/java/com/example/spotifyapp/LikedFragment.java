package com.example.spotifyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.example.spotifyapp.Connectors.SongService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikedFragment extends Fragment implements OnClickListener{
    TextView likedView;
    Button playlistButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences sharedPreferences;
    String likedSongIDs = "";
    private RequestQueue queue;
    private SongService songService;

    public LikedFragment(Context context){
        songService = new SongService(context);
    }

    private void loadData(View v, String id){
        // docRef = db.collection("Notebook").document(id).collection("likedSongs");

        db.collection("Notebook").document(sharedPreferences.getString("userid", "No User")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String text = "";
                        String dislikedText = "";
                        if(documentSnapshot.exists()){
                            Map<String, String> map = new HashMap<>();
                            List<Map> liked = (List<Map>) documentSnapshot.get("likedSongs");
                            for(Map song : liked){
                                text += song.get("name")+ " - " + song.get("artist") + "\n";
                                likedSongIDs += song.get("id") + "%2C";
                            }
                            likedView.setText(text);

                        } else {
                            Toast.makeText(v.getContext(), "Document does not exist!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });


        //likedView.setText(text[0]);
    }

    public void saveSongs(View view, String likedSongIDs){
        Toast.makeText(view.getContext(), "Added", Toast.LENGTH_SHORT);
        songService.addSongsToLibrary(likedSongIDs);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        likedSongIDs = "";
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_like, container, false);
        likedView = view.findViewById(R.id.likedSongs);
        playlistButton = view.findViewById(R.id.playListBtn);
        Log.e("WAT", playlistButton.getText().toString());
        sharedPreferences = view.getContext().getSharedPreferences("SPOTIFY", 0);
        String id = sharedPreferences.getString("userid", "No User");
        loadData(view, id);
        playlistButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playListBtn:
                saveSongs(v, likedSongIDs);
                break;
        }
    }
}
