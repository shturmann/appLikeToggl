package com.madappgang.appliketoggl;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import static android.os.SystemClock.uptimeMillis;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button pauseButton;
    private Button stopButton;
    private TextView txtTimer;
    private LinearLayout container;
    Handler customHandler = new Handler();

    long startTime = 0L, timeInMilliseconds = 0L, timeSwapBuff = 0L, updateTime = 0L;

    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            secs %= 60;
            int hours = mins / 60;
            txtTimer.setText(""+hours+":"+String.format("%02d", mins)+":"+String.format("%02d", secs));
            customHandler.postDelayed(this,0);
        }
    };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            startButton = (Button) findViewById(R.id.startButton);
            pauseButton = (Button) findViewById(R.id.pauseButton);
            stopButton = (Button) findViewById(R.id.stopButton);
            txtTimer = (TextView) findViewById(R.id.timerValueTextView);
            container = (LinearLayout) findViewById(R.id.container);

            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startTime = uptimeMillis();

                   customHandler.postDelayed(updateTimerThread, 0);
                }
            });

            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                }
            });

            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View addView = inflater.inflate(R.layout.results,null);
                    TextView txtValue = (TextView)addView.findViewById(R.id.timeLeftValue);
                    txtValue.setText(txtTimer.getText());
                    container.addView(addView);
                    txtTimer.setText("0:00:00");
                    customHandler.removeCallbacks(updateTimerThread);
                    startTime = 0;
                    timeSwapBuff = 0;
                    timeInMilliseconds = 0;
                }
            });


        }

  /**     public void showPopup(View v) {
            PopupMenu popup = new PopupMenu(this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.actions, popup.getMenu());
            popup.show();
        }*/
}