package com.example.matchmaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatchgameActivity extends AppCompatActivity {

    Bundle gameBoard = new Bundle();
    Bundle selectedCard = new Bundle();
    List<Integer> gameArray;
    int clickCnt;
    int matchCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generateMatchArray();
        clickCnt = 0;
        matchCnt = 0;

        setContentView(R.layout.activity_matchgame);
        GridLayout gridLayout = findViewById(R.id.rooLayout);
        int column = getIntent().getIntExtra("column",4);
        gridLayout.setColumnCount(column);

        for (int i=0; i<column*column; i++){
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            );

            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(param);
            imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.cardback, null));
            imageView.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.cardback_round, null));
            gridLayout.addView(imageView);
            final int finalI = i;

            if (gameArray.size() <= 0){
                gameBoard.putInt(String.valueOf(i), 0);
            }
            else {
                gameBoard.putInt(String.valueOf(i), getRandomElement());
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MatchgameActivity.this, "Card Value: "+ getCardValue(finalI), Toast.LENGTH_SHORT).show();
                    ImageView thisView = (ImageView) v;

                    // ADD ANIMATION TO VIEW
                    animateCard(thisView);

                    // UPDATE CLICK COUNT
                    TextView textView = findViewById(R.id.clickCnt);
                    textView.setText("Clicks: " + Integer.toString(++clickCnt));

                    if (selectedCard.get("CurrentCard")!=null && selectedCard.getInt("CurrentCard")!=finalI)
                    {
                        // A card is selected, so check if there is a match here
                        if (checkMatch(finalI)){

                            Log.d("Match Made", "Matched Card " + selectedCard.get("CurrentCard") + " With " + finalI);
                            thisView.setVisibility(View.INVISIBLE);
                            // thisView.setVisibility(View.GONE);
                        }
                    }
                    else{
                        // Set this as current card
                        Log.d("Current Card", "Currently selected card is " + finalI + " With value " + getCardValue(finalI));
                        selectedCard.putInt("CurrentCard", finalI);
                    }
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(MatchgameActivity.this, "i="+ finalI, Toast.LENGTH_SHORT).show();
                    ImageView thisView = (ImageView) v;
                    thisView.setRotation(imageView.getRotation() + 180);
                    return true;
                }
            });
        }
    }

    private void animateCard(View thisView){
        // ADD ANIMATION TO VIEW
        thisView.animate();
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(500);
        rotate.setInterpolator(new LinearInterpolator());
        thisView.startAnimation(rotate);
    }

    private void flipCardImg(View v, int viewIndx){
        ImageView thisView = (ImageView) v;

        // If the Cardback is showing
        animateCard(thisView);
        if (thisView.getDrawable()==ResourcesCompat.getDrawable(getResources(), R.mipmap.cardback, null)){
            // Set view image as the card number
            int cardVal = getCardValue(viewIndx);
            // thisView.set
        }
        else{
            thisView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.cardback, null));
            // thisView.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.cardback_round, null));
        }
    }
    //
    // The int represents the dict key for the card clicked
    //
    private boolean checkMatch(int checkCardIndx){
        int cardVal = getCardValue(checkCardIndx);

        if (cardVal == getCardValue(selectedCard.getInt("CurrentCard")))
        {
            Log.d("Match Made", "Made match with " + checkCardIndx +" and "+ selectedCard.getInt("CurrentCard"));
            Log.d("Match Values", "Matched on " + cardVal);

            // UPDATE CLICK COUNT
            TextView textView = findViewById(R.id.matchCnt);
            textView.setText("Matches: "+Integer.toString(++matchCnt));
            Toast.makeText(MatchgameActivity.this, "Matched Card "
                    + checkCardIndx + " With " + selectedCard.getInt("CurrentCard")
                    + " On Number "+cardVal, Toast.LENGTH_SHORT).show();

            // deactivate both cards
            selectedCard.clear();
            return true;
        }
        else
        {
            // clear current selected card, and turn both cards back over
            Log.d("NO Match Made", "Did Not Match Card " + selectedCard.get("CurrentCard") + " With " + checkCardIndx);
            Log.d("Reset Selected Card", "Cards Reset");
            Toast.makeText(MatchgameActivity.this, "No Match with " + checkCardIndx + " and " + selectedCard.getInt("CurrentCard") + ". Resetting Pair.", Toast.LENGTH_SHORT).show();
            selectedCard.clear();
            return false;
        }
    }

    // Fill the game array with the number of pairs to be matched
    //
    private void generateMatchArray(){
        int column = getIntent().getIntExtra("column",4);
        int numPairs = (column*column)/2;
        gameArray = new ArrayList<>();

        for (int i=0; i<numPairs; i++){
            // Add a Pair of numbers to be taken later for matching
            //
            gameArray.add(i+1);
            gameArray.add(i+1);
        }
    }

    // Function select an element base on index
    // and return an element
    private int getRandomElement()
    {
        Random rand = new Random();
        Log.d("Game Array Size", "Here: " + gameArray.size());
        int randIndx = rand.nextInt(gameArray.size());
        int value = gameArray.get(randIndx);
        gameArray.remove(randIndx);
        return value;
    }

    private int getCardValue(int checkCardIndx){
        int cardVal = gameBoard.getInt(String.valueOf(checkCardIndx));
        return cardVal;
    }
}