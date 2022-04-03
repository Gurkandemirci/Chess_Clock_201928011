package com.example.assignment2_201928011;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sun.xml.bind.Util;

import org.antlr.v4.misc.Utils;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private static long Start_TIME_IN_MILLIS = 60000;
    private Button mStartPauseButton;
    private Button mResetButton;
    private EditText inputTime;
    private TextView mCountDownBlack;
    private TextView mCountDownWhite;
    private Button mPassButtonBlack;
    private Button mPassButtonWhite;
    private CountDownTimer mCountDownTimerBlack;
    private CountDownTimer mCountDownTimerWhite;
    private boolean isTimerProgress=false;
    private boolean isStarted=false;
    private boolean isWhiteTurn=true;
    private boolean isBlackTurn=false;
    private long mTimeLeftInMillisBlack = Start_TIME_IN_MILLIS*10;
    private long mTimeLeftInMillisWhite = Start_TIME_IN_MILLIS*10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mStartPauseButton = findViewById(R.id.button_start_pause);
        mResetButton = findViewById(R.id.button_reset);
        inputTime = findViewById(R.id.input_minute_text);
        mCountDownBlack = findViewById(R.id.countdown_black);
        mCountDownWhite = findViewById(R.id.countdown_white);
        mPassButtonBlack = findViewById(R.id.pass_button_black);
        mPassButtonWhite = findViewById(R.id.pass_button_white);


        mStartPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(inputTime.getText())&&!isStarted) {
                    String time = inputTime.getText().toString();
                    mCountDownBlack.setText(time + ":00");
                    mCountDownWhite.setText(time + ":00");
                    mTimeLeftInMillisBlack = Start_TIME_IN_MILLIS * Integer.parseInt(time);
                    mTimeLeftInMillisWhite = Start_TIME_IN_MILLIS * Integer.parseInt(time);
                    isStarted=true;
                }
                if(!isTimerProgress){
                    inputTime.setVisibility(View.INVISIBLE);
                    mCountDownBlack.setVisibility(View.VISIBLE);
                    mCountDownWhite.setVisibility(View.VISIBLE);
                    if(isWhiteTurn){
                        mPassButtonWhite.setVisibility(View.VISIBLE);
                        startTimerWhite();
                    }
                    else if(isBlackTurn){
                        mPassButtonBlack.setVisibility(View.VISIBLE);
                        startTimerBlack();
                    }
                    isTimerProgress=true;
                }
                else{
                    mCountDownTimerWhite.cancel();
                    mCountDownTimerBlack.cancel();
                    mStartPauseButton.setText("Start");
                    isTimerProgress=false;
                    mResetButton.setVisibility(View.VISIBLE);
                    mPassButtonWhite.setVisibility(View.INVISIBLE);
                    mPassButtonBlack.setVisibility(View.INVISIBLE);
                }

            }
        });

        mPassButtonWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownTimerWhite.cancel();
                mPassButtonBlack.setVisibility(View.VISIBLE);
                mPassButtonWhite.setVisibility(View.INVISIBLE);
                startTimerBlack();
                isWhiteTurn=false;
                isBlackTurn=true;

            }
        });

        mPassButtonBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownTimerBlack.cancel();
                mPassButtonWhite.setVisibility(View.VISIBLE);
                mPassButtonBlack.setVisibility(View.INVISIBLE);
                startTimerWhite();
                isWhiteTurn=true;
                isBlackTurn=false;

            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTime.setVisibility(View.VISIBLE);
                inputTime.setText("");
                mResetButton.setVisibility(View.INVISIBLE);
                isWhiteTurn=true;
                isBlackTurn=false;
                isStarted=false;
            }
        });
    }
        private void startTimerWhite () {
            mCountDownTimerWhite = new CountDownTimer(mTimeLeftInMillisWhite, 1000) {
                @Override
                public void onTick(long milisUntilFinished) {
                    mTimeLeftInMillisWhite = milisUntilFinished;
                    updateCountDownTextWhite();
                }

                @Override
                public void onFinish() {
                    mResetButton.setVisibility(View.VISIBLE);
                    isTimerProgress=false;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Done");
                    builder.setMessage("Winner is Black!!");
                    builder.show();
                }
            }.start();
            mStartPauseButton.setText("pause");
            mResetButton.setVisibility(View.INVISIBLE);
        }

        private void updateCountDownTextWhite () {
            int minutes = (int) (mTimeLeftInMillisWhite / 1000) / 60;
            int seconds = (int) (mTimeLeftInMillisWhite / 1000) % 60;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            mCountDownWhite.setText(timeLeftFormatted);
        }

    private void startTimerBlack () {
        mCountDownTimerBlack = new CountDownTimer(mTimeLeftInMillisBlack, 1000) {
            @Override
            public void onTick(long milisUntilFinished) {
                mTimeLeftInMillisBlack = milisUntilFinished;
                updateCountDownTextBlack();
            }

            @Override
            public void onFinish() {
                mResetButton.setVisibility(View.VISIBLE);
                isTimerProgress=false;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Done");
                builder.setMessage("Winner is White!!");
                builder.show();
            }
        }.start();
        mStartPauseButton.setText("Pause");
        mResetButton.setVisibility(View.INVISIBLE);
    }

    private void updateCountDownTextBlack () {
        int minutes = (int) (mTimeLeftInMillisBlack / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillisBlack / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mCountDownBlack.setText(timeLeftFormatted);
    }

    private void changeTheme(boolean isWhiteTurn,View root){
        if(isWhiteTurn){

            mPassButtonWhite.setBackgroundColor(Color.BLACK);
            mPassButtonWhite.setTextColor(Color.WHITE);
            mCountDownWhite.setHintTextColor(Color.BLACK);
            mCountDownBlack.setHintTextColor(Color.BLACK);
            mStartPauseButton.setBackgroundColor(Color.BLACK);
            mStartPauseButton.setTextColor(Color.WHITE);
        }

        else{

            mPassButtonBlack.setBackgroundColor(Color.WHITE);
            mPassButtonBlack.setTextColor(Color.BLACK);
            mCountDownWhite.setHintTextColor(Color.WHITE);
            mCountDownBlack.setHintTextColor(Color.WHITE);
            mStartPauseButton.setBackgroundColor(Color.WHITE);
            mStartPauseButton.setTextColor(Color.BLACK);
        }
    }
}

