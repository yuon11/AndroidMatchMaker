package com.example.matchmaker;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
            imageView.setTag(R.mipmap.cardback);
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

                    // Card flip animation
                    flipCardImg(thisView, finalI);

                    // UPDATE CLICK COUNT
                    updateClickCnt();

                    // Update Cards if a match is made
                    updateGameBoard(finalI, v);

                    // Check game state
                    checkForWin();
                }
            });
        }
    }

    // Update Click View
    //
    private void updateClickCnt(){
        TextView textView = findViewById(R.id.clickCnt);
        textView.setText("Clicks: " + Integer.toString(++clickCnt));
    }

    // Run through the step for updating the game board
    //
    private void updateGameBoard(int finalI, View v){

        GridLayout gridLayout = findViewById(R.id.rooLayout);
        ImageView thisView = (ImageView) v;
        ImageView matchedView = (ImageView) gridLayout.getChildAt(selectedCard.getInt("CurrentCard"));

        // Set Card as currently selected
        //
        if (selectedCard.get("CurrentCard")==null){
            Log.d("Current Card", "Currently selected card is " + finalI + " With value " + getCardValue(finalI));
            selectedCard.putInt("CurrentCard", finalI);
            return;
        }
        // Check for match since selected and clicked on is different
        //
        if (selectedCard.get("CurrentCard")!=null && selectedCard.getInt("CurrentCard")!=finalI){

            if (checkMatch(finalI)){
                Log.d("Match Made", "Matched Card " + selectedCard.get("CurrentCard") + " With " + finalI);
                thisView.setVisibility(View.INVISIBLE);
                matchedView.setVisibility(View.INVISIBLE);
            }
            else{
                Log.d("Mismatch", "No match made is " + finalI + " Prior card is" + selectedCard.get("CurrentCard"));
                flipCardImg(matchedView, selectedCard.getInt("CurrentCard"));
                flipCardImg(thisView, finalI);
            }
            selectedCard.clear();
        }

        else if (selectedCard.get("CurrentCard")!=null && selectedCard.getInt("CurrentCard")==finalI){
            selectedCard.clear();
        }
        else{
            Log.d("INVALID MOVE", "Something unexpected happened");
        }

    }

    // ADD ANIMATION TO VIEW
    //
    private void animateCard(View thisView){
        thisView.animate();
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setInterpolator(new LinearInterpolator());
        thisView.startAnimation(rotate);
    }

    private void flipCardImg(View v, int viewIndx){
        // If the Cardback is showing
        //
        ImageView thisView = (ImageView) v;
        animateCard(thisView);

        // Check If card is currently showing back logo
        if (((Integer) thisView.getTag()== R.mipmap.cardback)){
            int cardVal = getCardValue(viewIndx);
            Log.d("Flip Image", "Changing drawable in IF statement");
            thisView.setTag(returnObjTag(cardVal));
            thisView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), (Integer) returnObjTag(cardVal), null));
        }
        else{
            Log.d("Flip Image", "Changing drawable in ELSE statement");

            thisView.setTag(R.mipmap.cardback);
            thisView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.cardback, null));
        }
    }

    // Return reference to necessary image number pic
    //
    public Object returnObjTag(int cardVal){
        Object imgTag = R.mipmap.zero;
        switch (cardVal) {
            case 1:  imgTag = R.mipmap.one;
                break;
            case 2:  imgTag = R.mipmap.two;
                break;
            case 3:  imgTag = R.mipmap.three;
                break;
            case 4:  imgTag = R.mipmap.four;
                break;
            case 5:  imgTag = R.mipmap.five;
                break;
            case 6:  imgTag = R.mipmap.six;
                break;
            case 7:  imgTag = R.mipmap.seven;
                break;
            case 8:  imgTag = R.mipmap.eight;
                break;
            case 9:  imgTag = R.mipmap.nine;
                break;
            case 10: imgTag = R.mipmap.ten;
                break;
            case 11: imgTag = R.mipmap.eleven;
                break;
            case 12: imgTag = R.mipmap.twelve;
                break;
            case 13:  imgTag = R.mipmap.thirteen;
                break;
            case 14:  imgTag = R.mipmap.fourteen;
                break;
            case 15:  imgTag = R.mipmap.fifteen;
                break;
            case 16:  imgTag = R.mipmap.sixteen;
                break;
            case 17:  imgTag = R.mipmap.seventeen;
                break;
            case 18: imgTag = R.mipmap.eighteen;
                break;
            case 19: imgTag = R.mipmap.ninteen;
                break;
            case 20: imgTag = R.mipmap.twenty;
                break;
            case 21: imgTag = R.mipmap.twentyone;
                break;
            case 22: imgTag = R.mipmap.twentytwo;
                break;
            case 23:  imgTag = R.mipmap.twentythree;
                break;
            case 24:  imgTag = R.mipmap.twentyfour;
                break;
            case 25: imgTag = R.mipmap.twentyfive;
                break;
            default: imgTag = R.mipmap.zero;
                break;
        }
        return imgTag;
    }

    // Check Game board for win condition
    //
    private void checkForWin(){
        int column = getIntent().getIntExtra("column",4);
        int numPairs = (column*column)/2;

        if (matchCnt==numPairs)
        {
            Intent intent = new Intent(this, WinActivity.class);
            intent.putExtra("score", clickCnt);
            intent.putExtra("difficulty", column);
            startActivity(intent);
        }
    }

    // UPDATE MATCH COUNT
    private void updateMatchCnt(){
        TextView textView = findViewById(R.id.matchCnt);
        textView.setText("Matches: "+Integer.toString(++matchCnt));
    }

    //
    // The int represents the dict key for the card clicked
    //
    private boolean checkMatch(int checkCardIndx){
        int cardVal = getCardValue(checkCardIndx);
        int selectedCardVal = getCardValue(selectedCard.getInt("CurrentCard"));

        if (cardVal == selectedCardVal)
        {
            Log.d("Match Made", "Made match with " + checkCardIndx +" and "+ selectedCard.getInt("CurrentCard"));
            Log.d("Match Values", "Matched on " + cardVal);
            updateMatchCnt();
            Toast.makeText(MatchgameActivity.this, "Matched Card "
                    + checkCardIndx + " With " + selectedCard.getInt("CurrentCard")
                    + " On Number "+cardVal, Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            // clear current selected card, and turn both cards back over
            Log.d("No Match Made", "Did Not Match Card " + selectedCard.get("CurrentCard") + " With " + checkCardIndx);
            Log.d("Reset Selected Card", "Cards Reset");
            Toast.makeText(MatchgameActivity.this, "No Match with " + checkCardIndx + " and " + selectedCard.getInt("CurrentCard") + ". Resetting Pair.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Fill the game array with the number of pairs to be matched
    //
    private void generateMatchArray(){
        int column = getIntent().getIntExtra("column",4);
        int numPairs = (column*column)/2;
        gameArray = new ArrayList<>();

        //
        // 0 acts as our 'joker' when row*col is odd
        //
        if ( column*column % 2 != 0 )
            gameArray.add(0);

        // Add a Pair of numbers to be taken later for matching
        //
        for (int i=0; i<numPairs; i++){
            gameArray.add(i+1);
            gameArray.add(i+1);
        }
    }

    // Function select an element base on index and return an element
    //
    private int getRandomElement()
    {
        Random rand = new Random();
        Log.d("Game Array Size", "Here: " + gameArray.size());
        int randIndx = rand.nextInt(gameArray.size());
        int value = gameArray.get(randIndx);
        gameArray.remove(randIndx);
        return value;
    }

    // Check the gameboard for the card value assigned at game start
    //
    private int getCardValue(int checkCardIndx){
        return gameBoard.getInt(String.valueOf(checkCardIndx));
    }
}