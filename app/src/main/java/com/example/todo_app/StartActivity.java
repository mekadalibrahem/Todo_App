package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StartActivity extends AppCompatActivity {

    public  final int numberOfDots = 4 ;
    final int delayBetweenDots = 500;
    public ImageView[] im_v_dots;
     public LinearLayout linear_dots ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        linear_dots = findViewById(R.id.start_dots);
        im_v_dots = new ImageView[numberOfDots];
        //add dots for activity
        for (int i = 0; i < numberOfDots; i++) {
            im_v_dots[i] = new ImageView(this);
            im_v_dots[i].setImageResource(R.drawable.dot_empty);
            im_v_dots[i].setLayoutParams(new LinearLayout.LayoutParams(30, 30, 1.0f));
            im_v_dots[i].setPadding(4, 0, 8, 0); // Adjust padding as needed

            linear_dots.addView(im_v_dots[i]);
        }

        // Loop to color dots sequentially with a delay between each

        for (int i = 0; i < numberOfDots; i++) {
            final int dotIndex = i; // Capture index for inner Runnable

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    im_v_dots[dotIndex].setImageResource(R.drawable.dot_colored);
                }
            }, i * delayBetweenDots);
        }


        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                          startActivity(intent);
                                          finish();
                                      }},
                numberOfDots * delayBetweenDots);


    }


}