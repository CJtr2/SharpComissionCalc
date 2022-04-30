package com.example.comissioncalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //notes:
        /*
        on image view pressed change between toggled on text and toggled off text, make custom image for it
        check all others to make sure they aren't enabled on press
        have array of booleans


        replace full body/half body/headshot with custom text


        each togglable box is a different base price
        (base price * (body type modifier)) + flat [apply discounts after this]

        # discounts:
        #full + (#half)*(2/3) + (#head)/2
        or to make decimal math less dangerous:

        every full
        every 2nd and 3rd half body
        every other head

        + floor((#half%3)/3 + (#head%2)/2)

        apply to lowest first

        */
    }
}