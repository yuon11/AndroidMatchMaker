package com.example.matchmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startBeginnerGame(View view) {
        Intent intent = new Intent(view.getContext(), MatchgameActivity.class);
        intent.putExtra("column", 3);
        startActivity(intent);
    }

    public void startIntermediateGame(View view) {
        Intent intent = new Intent(view.getContext(), MatchgameActivity.class);
        intent.putExtra("column", 4);
        startActivity(intent);
    }

    public void startAdeptGame(View view) {
        Intent intent = new Intent(view.getContext(), MatchgameActivity.class);
        intent.putExtra("column", 5);
        startActivity(intent);
    }

}