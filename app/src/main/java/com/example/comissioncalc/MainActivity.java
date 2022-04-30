package com.example.comissioncalc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //activity declarations=================================
    public InputMethodManager imm;

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


        //activity elements declaration\
        imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);

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


        //internal declarations (will be fetched from config)
        basePrice = 60.0;
        flatPrice = 30.0;
        discountPer1 = 1;
        discountpercent = 50;

        hbNumer = 2;
        hbDenom = 3;

        hsNumer = 1;
        hsDenom = 2;


        //do stuff
        baseText.setText("$ " + String.format(Locale.ROOT,"%,.0f", basePrice));
        flatText.setText("$ " + String.format(Locale.ROOT,"%,.0f", flatPrice));


        View.OnKeyListener updatePrice = (view, keyCode, keyEvent) -> {
            if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                updatePriceText();
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
            }else{
                updatePriceText();
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

        //Data Fetching===========================================
        String temp = fbEntry.getText().toString();
        if(temp.equals("")){fb = 0;}
        else{fb = Integer.parseInt(temp);}

        temp = hbEntry.getText().toString();
        if(temp.equals("")){hb = 0;}
        else{hb = Integer.parseInt(temp);}

        temp = hsEntry.getText().toString();
        if(temp.equals("")){hs = 0;}
        else{hs = Integer.parseInt(temp);}


        //# of Discount Calculation================================
        int numOfDiscounts = fb;

        for(int i = 1; i <= hb; i++){
            if(i%hbDenom < hbNumer){
                numOfDiscounts++;
            }
        }
        for(int j = 1; j <= hs; j++){
            if(j%hsDenom < hsNumer){
                numOfDiscounts++;
            }
        }

        numOfDiscounts += + Math.floor(((hb*hbNumer)%hbDenom)/((double)hbDenom) + ((hs*hsNumer)%hsDenom)/(double)hsDenom);

        int tempDis = numOfDiscounts;
        int[] discounts = new int[] {0, 0, 0};

        if(tempDis <= hs){
            discounts[2] = tempDis;
        }
        else{
            discounts[2] = hs;
            tempDis -= hs;

            if(tempDis <= hb){
                discounts[1] = tempDis;
            }
            else{
                discounts[1] = hb;
                tempDis -= hb;
                discounts[0] = tempDis;
            }
        }


        //full body section====================================
        if(false){

        }

        //half body section====================================
        if(false){

        }

        //headshot section=====================================
        if(false){

        }


        pricesFieldText.setText(out);
    }
}