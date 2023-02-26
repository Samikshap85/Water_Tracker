package com.example.mywatertracker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {


    private int count;
    private int goal=10;

    private TextView countTextView;
    private Button button , resetbutton;

    private ProgressBar progressBar;
    private DatabaseReference cupsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        countTextView = findViewById(R.id.text_view_progress);
        button = findViewById(R.id.button);
        resetbutton=findViewById(R.id.resetbutton);
        progressBar = findViewById(R.id.progress_bar);


        cupsRef = FirebaseDatabase.getInstance().getReference().child("cups");
        cupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    count = dataSnapshot.getValue(Integer.class);
                    countTextView.setText(count + " cups");
                    progressBar.setProgress(count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", "onCancelled", databaseError.toException());
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                countTextView.setText(count + " cups");
                cupsRef.setValue(count);

                progressBar.setProgress(count);
            }
        });
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=0;
                countTextView.setText(count+"cups");
                cupsRef.setValue(count);
                progressBar.setProgress(count);
            }
        });
        progressBar.setMax(goal);
        progressBar.setProgress(count);
    }
}
