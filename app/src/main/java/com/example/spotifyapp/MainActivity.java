package com.example.spotifyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button add;
    private Button reset;
    private Button red;
    private Button black;
    private Button blue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView countText = findViewById(R.id.count);
        final RelativeLayout layout = findViewById(R.id.layout);
        add = findViewById(R.id.add);
        reset = findViewById(R.id.reset);
        red = findViewById(R.id.redButton);
        black = findViewById(R.id.blackButton);
        blue = findViewById(R.id.blueButton);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int countNum = Integer.parseInt(countText.getText().toString());
                countText.setText(countNum + 1 + "");
            }
        });
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                countText.setText(0 + "");
                layout.setBackgroundColor(Color.GRAY);
            }
        });

        red.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                layout.setBackgroundColor(Color.RED);
            }
        });
        black.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                layout.setBackgroundColor(Color.BLACK);
            }
        });
        blue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                layout.setBackgroundColor(Color.BLUE);
            }
        });

    }
}