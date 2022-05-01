package com.example.comissioncalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    public Button shading;

    public RelativeLayout shdCtn;

    public ImageView shadedBtn;
    public ImageView celBtn;
    public ImageView flatsBtn;
    public ImageView linesBtn;
    public ImageView sketchBtn;
    public ImageView tgmStickBtn;

    //internal declarations===============================
    public int fb;
    public int hb;
    public int hs;

    public double basePrice;
    public double flatPrice;

    public double shaded;
    public double cel;
    public double flats;
    public double lines;
    public double sketch;
    public double tgmStick;

    public int discountPer1;
    public int discountpercent;

    public int hbNumer;
    public int hbDenom;
    public int hsNumer;
    public int hsDenom;

    public double preExtraFinal;

    public ImageView[] shadeArray;
    public double[] prices;
    public boolean[] shadeActive;
    public String[] shadeNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize declarations
        preExtraFinal = 0;
        shadeArray = new ImageView[6];
        prices = new double[6];
        shadeActive = new boolean[] {true, false, false, false, false, false};
        shadeNames = new String[] {"Shaded","Cel","Flats","Lines","Sketch","Tgm"};

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
        shading = findViewById(R.id.shadingBtn);

        shdCtn = findViewById(R.id.shadingContainer);

        shadeArray[0] = shadedBtn = findViewById(R.id.shadedBtn);
        shadeArray[1] =celBtn = findViewById(R.id.celBtn);
        shadeArray[2] =flatsBtn = findViewById(R.id.flatsBtn);
        shadeArray[3] =linesBtn = findViewById(R.id.linesBtn);
        shadeArray[4] =sketchBtn = findViewById(R.id.sketchBtn);
        shadeArray[5] =tgmStickBtn = findViewById(R.id.tgmStickBtn);

        shadedBtn.setImageDrawable(ResourcesCompat.getDrawable(this.getResources(), android.R.drawable.btn_star_big_on, null));

        //internal declarations (will be fetched from config)
        basePrice = 60.0;
        flatPrice = 30.0;

        prices[0] = shaded = 60;
        prices[1] = cel = 50;
        prices[2] = flats = 45;
        prices[3] = lines = 35;
        prices[4] = sketch = 25;
        prices[5] = tgmStick = 50;

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


        copy.setOnClickListener(view -> copyText());


        shading.setOnClickListener(view -> {
            boolean temp = View.VISIBLE != shdCtn.getVisibility();

            if(temp){
                shdCtn.setVisibility(View.VISIBLE);
            }else{
                shdCtn.setVisibility(View.INVISIBLE);
            }
        });


        shadedBtn.setOnClickListener(view -> toggleShade(0));
        celBtn.setOnClickListener(view -> toggleShade(1));
        flatsBtn.setOnClickListener(view -> toggleShade(2));
        linesBtn.setOnClickListener(view -> toggleShade(3));
        sketchBtn.setOnClickListener(view -> toggleShade(4));
        tgmStickBtn.setOnClickListener(view -> toggleShade(5));


        //notes:
        /*
        on image view pressed change between toggled on text and toggled off text, make custom image for it
        check all others to make sure they aren't enabled on press
        have array of booleans

        change text of shading button to what shading is selected

        each togglable box is a different base price


        replace full body/half body/headshot with custom text
        */
    }

    public void updatePriceText(){
        preExtraFinal = 0;

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


        //# of Discount Calculation/Distribution=====================
        int numOfDiscounts = ((fb+hb+hs)*discountPer1)/(discountPer1+1);

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

        int usedDisc = numOfDiscounts;

        //full body section====================================
        if(fb != 0){
            out += "================================\n";
            out += "Full Body:\n";
            out += "================================\n";
            double cost = (basePrice + flatPrice);
            double discAmnt = cost - (cost * (discountpercent/100.0));

            double dscTot = 0;
            double costTot = 0;

            int numDigits = 1;
            if(fb > 99){
                numDigits = 3;
            }else if(fb > 9){
                numDigits = 2;
            }

            String format = "%0" + numDigits + "d";

            for(int q = 0; q < fb; q++){
                out += "Full Body " + String.format(Locale.ROOT,format, q+1) + ":    $ " + String.format(Locale.ROOT,"%,.2f", cost) + "\n";
                costTot += cost;

                if(discounts[0] > 0){
                    out += "                                      -$ " + String.format(Locale.ROOT,"%,.2f", discAmnt) + "  dsc[" + usedDisc +"]\n";
                    usedDisc--;
                    dscTot += discAmnt;
                    discounts[0]--;
                }
            }

            preExtraFinal += costTot;
            preExtraFinal -= dscTot;
            out += "\n================================\n";
            out +="Cost:                $ " + String.format(Locale.ROOT,"%,.2f", costTot) + "\n";
            out +="                                      -$ " + String.format(Locale.ROOT,"%,.2f", dscTot) + "\n";
            out +="SubTotal:        $ " + String.format(Locale.ROOT,"%,.2f", preExtraFinal) + "\n\n";
        }

        //half body section====================================
        if(hb != 0){
            out += "================================\n";
            out += "Half Body:\n";
            out += "================================\n";

            double cost = (((basePrice * hbNumer)/hbDenom)+ flatPrice);
            double discAmnt = cost - (cost * (discountpercent/100.0));

            double dscTot = 0;
            double costTot = 0;

            int numDigits = 1;
            if(hb > 99){
                numDigits = 3;
            }else if(hb > 9){
                numDigits = 2;
            }

            String format = "%0" + numDigits + "d";

            for(int q = 0; q < hb; q++){
                out += "Half Body " + String.format(Locale.ROOT,format, q+1) + ":    $ " + String.format(Locale.ROOT,"%,.2f", cost) + "\n";
                costTot += cost;

                if(discounts[1] > 0){
                    out += "                                      -$ " + String.format(Locale.ROOT,"%,.2f", discAmnt) + "  dsc[" + usedDisc +"]\n";
                    usedDisc--;
                    dscTot += discAmnt;
                    discounts[1]--;
                }
            }

            preExtraFinal += costTot;
            preExtraFinal -= dscTot;
            out += "\n================================\n";
            out +="Cost:                $ " + String.format(Locale.ROOT,"%,.2f", costTot) + "\n";
            out +="                                      -$ " + String.format(Locale.ROOT,"%,.2f", dscTot) + "\n";
            out +="SubTotal:        $ " + String.format(Locale.ROOT,"%,.2f", preExtraFinal) + "\n\n";
        }

        //headshot section=====================================
        if(hs != 0){
            out += "================================\n";
            out += "Headshot:\n";
            out += "================================\n";

            double cost = (((basePrice * hsNumer)/hsDenom)+ flatPrice);
            double discAmnt = cost - (cost * (discountpercent/100.0));

            double dscTot = 0;
            double costTot = 0;

            int numDigits = 1;
            if(hs > 99){
                numDigits = 3;
            }else if(hs > 9){
                numDigits = 2;
            }

            String format = "%0" + numDigits + "d";

            for(int q = 0; q < hs; q++){
                out += "Headshot " + String.format(Locale.ROOT,format, q+1) + ":    $ " + String.format(Locale.ROOT,"%,.2f", cost) + "\n";
                costTot += cost;

                if(discounts[2] > 0){
                    out += "                                      -$ " + String.format(Locale.ROOT,"%,.2f", discAmnt) + "  dsc[" + usedDisc +"]\n";
                    usedDisc--;
                    dscTot += discAmnt;
                    discounts[2]--;
                }
            }

            preExtraFinal += costTot;
            preExtraFinal -= dscTot;
            out += "\n================================\n";
            out +="Cost:                $ " + String.format(Locale.ROOT,"%,.2f", costTot) + "\n";
            out +="                                      -$ " + String.format(Locale.ROOT,"%,.2f", dscTot) + "\n";
            out +="SubTotal:        $ " + String.format(Locale.ROOT,"%,.2f", preExtraFinal) + "\n\n";
        }
        if(fb+hb+hs != 0){out += "================================\n";}


        pricesFieldText.setText(out);
    }

    public void copyText(){
        ClipboardManager cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String temp = pricesFieldText.getText().toString();
        ClipData out = ClipData.newPlainText("output", temp);
        cb.setPrimaryClip(out);

        Toast.makeText(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
    }

    public void toggleShade(int shadeVal){
        shadeArray[shadeVal].setImageDrawable(ResourcesCompat.getDrawable(this.getResources(), android.R.drawable.btn_star_big_on, null));
        basePrice = prices[shadeVal];
        shadeActive[shadeVal] = true;
        baseText.setText("$ " + String.format(Locale.ROOT,"%,.0f", basePrice));
        shading.setText(shadeNames[shadeVal]);

        for(int i = 0; i < shadeArray.length; i++){
            if(i != shadeVal && shadeActive[i]){
                shadeActive[i] = false;
                shadeArray[i].setImageDrawable(ResourcesCompat.getDrawable(this.getResources(), android.R.drawable.btn_star_big_off, null));
            }
        }

        updatePriceText();
        shdCtn.setVisibility(View.INVISIBLE);
    }
}