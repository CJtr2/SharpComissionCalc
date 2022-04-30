package com.example.comissioncalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //activity declarations=================================
    public TextView baseText;
    public TextView flatText;

    public EditText fbEntry;
    public EditText hbEntry;
    public EditText hsEntry;

    public TextView pricesFieldText;
    public TextView finalpriceText;

    public Button config;
    public Button copy;
    public Button addExtra;
    public Button calculate;

    //internal declarations===============================
    public int fb;
    public int hb;
    public int hs;

    public double basePrice;
    public double flatPrice;

    public int discountPer1;
    public int discountpercent;

    public int hbNumer;
    public int hbDenom;
    public int hsNumer;
    public int hsDenom;

    public double preExtraFinal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //activity elements declaration
        baseText = findViewById(R.id.curBaseText);
        flatText = findViewById(R.id.curFlatText);

        fbEntry = findViewById(R.id.fullBodyNum);
        hbEntry = findViewById(R.id.halfBodyNum);
        hsEntry = findViewById(R.id.headshotNum);

        pricesFieldText = findViewById(R.id.pricesText);
        finalpriceText = findViewById(R.id.finalPriceText);

        config = findViewById(R.id.configBtn);
        copy = findViewById(R.id.copyBtn);
        addExtra = findViewById(R.id.addExtraBtn);
        calculate = findViewById(R.id.calcBtn);


        //internal declarations
        basePrice = 60.0;
        flatPrice = 30.0;


        //do stuff
        baseText.setText("$ " + String.format(Locale.ROOT,"%,.0f", basePrice));
        flatText.setText("$ " + String.format(Locale.ROOT,"%,.0f", flatPrice));


        View.OnKeyListener updatePrice = (view, keyCode, keyEvent) -> {
            if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                updatePriceText();
                return true;
            }
            return false;
        };

        View.OnClickListener clearText = view -> {
            if(view instanceof EditText){
                ((EditText) view).setText("");
            }
        };

        View.OnFocusChangeListener clearTextFocus = (view, hasFocus) -> {
            if(view instanceof  EditText && hasFocus){
                ((EditText) view).setText("");
            }
        };

        fbEntry.setOnKeyListener(updatePrice);
        hbEntry.setOnKeyListener(updatePrice);
        hsEntry.setOnKeyListener(updatePrice);

        fbEntry.setOnClickListener(clearText);
        hbEntry.setOnClickListener(clearText);
        hsEntry.setOnClickListener(clearText);

        fbEntry.setOnFocusChangeListener(clearTextFocus);
        hbEntry.setOnFocusChangeListener(clearTextFocus);
        hsEntry.setOnFocusChangeListener(clearTextFocus);

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

    public void updatePriceText(){
        String out = "";
        pricesFieldText.setText("Pressed: ");
    }
}