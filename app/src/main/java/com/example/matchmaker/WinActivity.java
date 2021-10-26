package com.example.matchmaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WinActivity  extends AppCompatActivity {

    Integer scoreText = 0;
    Integer difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win);

        scoreText = getIntent().getIntExtra("score", 0);
        difficulty = getIntent().getIntExtra("difficulty", 4);

        // Show Click count and score
        TextView textView = findViewById(R.id.clickCnt);
        textView.setText("Score: " + Integer.toString(scoreText) + "\n Difficulty: " + difficulty + "x" + difficulty);
    }

    public void playAgain(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}