package com.example.comissioncalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
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
    public TextView finalPriceText;
    public TextView finalDiscText;

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

    public LinearLayout extrasList;

    public EditText nameInput;
    public EditText extraPriceInput;

    public RelativeLayout extrasInputCtn;

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
    public int discountPercent;

    public int hbNumer;
    public int hbDenom;
    public int hsNumer;
    public int hsDenom;

    public double preExtraFinal;
    public double totalDiscount;

    public ImageView[] shadeArray;
    public double[] prices;
    public boolean[] shadeActive;
    public String[] shadeNames;

    public LinkedList<String> extras;
    public LinkedList<Double> extrasPrices;

    public int toEdit;
    public boolean readyToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize declarations
        preExtraFinal = 0;
        totalDiscount = 0;

        fb = 0;
        hb = 0;
        hs = 0;

        toEdit = -1;
        readyToDelete = false;

        shadeArray = new ImageView[6];
        prices = new double[6];
        shadeActive = new boolean[] {true, false, false, false, false, false};
        shadeNames = new String[] {"Shaded","Cel","Flats","Lines","Sketch","Tgm"};

        extras = new LinkedList<>();
        extrasPrices = new LinkedList<>();

        //activity elements declaration\
        imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        baseText = findViewById(R.id.curBaseText);
        flatText = findViewById(R.id.curFlatText);

        fbEntry = findViewById(R.id.fullBodyNum);
        hbEntry = findViewById(R.id.halfBodyNum);
        hsEntry = findViewById(R.id.headshotNum);

        pricesFieldText = findViewById(R.id.pricesText);
        finalPriceText = findViewById(R.id.finalPriceText);
        finalDiscText = findViewById(R.id.finalDiscTot);

        config = findViewById(R.id.configBtn);
        copy = findViewById(R.id.copyBtn);
        addExtra = findViewById(R.id.addExtraBtn);
        calculate = findViewById(R.id.calcBtn);
        shading = findViewById(R.id.shadingBtn);

        shdCtn = findViewById(R.id.shadingContainer);

        shadeArray[0] = shadedBtn = findViewById(R.id.shadedBtn);
        shadeArray[1] = celBtn = findViewById(R.id.celBtn);
        shadeArray[2] = flatsBtn = findViewById(R.id.flatsBtn);
        shadeArray[3] = linesBtn = findViewById(R.id.linesBtn);
        shadeArray[4] = sketchBtn = findViewById(R.id.sketchBtn);
        shadeArray[5] = tgmStickBtn = findViewById(R.id.tgmStickBtn);

        extrasList = findViewById(R.id.extrasList);

        nameInput = findViewById(R.id.nameInputText);
        extraPriceInput = findViewById(R.id.extraPriceInputText);
        extrasInputCtn = findViewById(R.id.extraInputContainer);

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
        discountPercent = 50;

        hbNumer = 2;
        hbDenom = 3;

        hsNumer = 1;
        hsDenom = 2;


        //set listeners and other starting operations
        updateBasePriceFlats(basePrice, flatPrice);

        copy.setOnClickListener(view -> copyText());
        calculate.setOnClickListener(view -> calculateTotal()); //TODO: Find new function for calculate button, since total auto updates

        {//body/headshot listeners
            TextView.OnEditorActionListener updatePrice = (textView, actionId, keyEvent) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updatePriceText();
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    return true;
                }
                return false;
            };

            View.OnClickListener clearText = view -> {
                if (view instanceof EditText) {
                    ((EditText) view).setText("");
                }
            };

            View.OnFocusChangeListener clearTextFocus = (view, hasFocus) -> {
                if (view instanceof EditText && hasFocus) {
                    ((EditText) view).setText("");
                } else {
                    updatePriceText();
                }
            };

            fbEntry.setOnEditorActionListener(updatePrice);
            hbEntry.setOnEditorActionListener(updatePrice);
            hsEntry.setOnEditorActionListener(updatePrice);

            fbEntry.setOnClickListener(clearText);
            hbEntry.setOnClickListener(clearText);
            hsEntry.setOnClickListener(clearText);

            fbEntry.setOnFocusChangeListener(clearTextFocus);
            hbEntry.setOnFocusChangeListener(clearTextFocus);
            hsEntry.setOnFocusChangeListener(clearTextFocus);
        }

        {//shade on click listeners
            shading.setOnClickListener(view -> toggleShadeContainer());

            for(int i = 0; i < shadeArray.length; i++){
                final int val = i;
                shadeArray[i].setOnClickListener(view -> toggleShade(val));
            }
        }

        {//add extra on listeners
            addExtra.setOnClickListener(view -> addExtraCtnToggle());

            nameInput.setOnEditorActionListener((textView, actionId, keyEvent) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    extraPriceInput.requestFocus();
                    imm.showSoftInput(extraPriceInput, InputMethodManager.SHOW_IMPLICIT);
                    return true;
                }
                return false;
            });

            extraPriceInput.setOnEditorActionListener((textView, actionId, keyEvent) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addExtraToList(textView);
                    return true;
                }
                return false;
            });
        }
    }

    //end main=========================================================================================================
    public void addExtraCtnToggle(){
        boolean temp = View.VISIBLE != extrasInputCtn.getVisibility();

        extraPriceInput.setText("");
        nameInput.setText("");

        if(temp){
            extrasInputCtn.setVisibility(View.VISIBLE);
            nameInput.requestFocus();
            imm.showSoftInput(nameInput, InputMethodManager.SHOW_IMPLICIT);
        }else{
            if(toEdit > -1) {
                toEdit = -1;
                readyToDelete = false;
                updateExtrasList();
            }
            extrasInputCtn.setVisibility(View.INVISIBLE);
        }
    }

    public void addExtraToList(TextView textView){
        String check = extraPriceInput.getText().toString();

        if(!"".equals(check) && !".".equals(check) && !"-".equals(check)){
            double price = (double)Math.round(100 * Double.parseDouble(check))/100.0;
            if(toEdit > -1){
                extrasPrices.set(toEdit, price);
                extras.set(toEdit, nameInput.getText().toString());

                toEdit = -1;
                readyToDelete = false;

                updateExtrasList();
            }else{
                extrasPrices.add(price);
                extras.add(nameInput.getText().toString());
            }

            extrasInputCtn.setVisibility(View.INVISIBLE);
            updateExtrasList();
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        }else{
            Toast.makeText(MainActivity.this, "Invalid Price Entered!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updatePriceText(){
        preExtraFinal = 0;
        totalDiscount = 0;

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
            double discAmnt = cost - (cost * (discountPercent /100.0));

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
            totalDiscount += dscTot;
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
            double discAmnt = cost - (cost * (discountPercent /100.0));

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
            totalDiscount += dscTot;
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
            double discAmnt = cost - (cost * (discountPercent /100.0));

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
            totalDiscount += dscTot;
            out += "\n================================\n";
            out +="Cost:                $ " + String.format(Locale.ROOT,"%,.2f", costTot) + "\n";
            out +="                                      -$ " + String.format(Locale.ROOT,"%,.2f", dscTot) + "\n";
            out +="SubTotal:        $ " + String.format(Locale.ROOT,"%,.2f", preExtraFinal) + "\n\n";
        }
        //if(fb+hb+hs != 0){out += "================================\n";}

        pricesFieldText.setText(out);

        calculateTotal();
    }

    public void updateBasePriceFlats(double base, double flat){
        basePrice = base;
        flatPrice = flat;

        String baseFlat = "$ " + String.format(Locale.ROOT,"%,.0f", basePrice);
        baseText.setText(baseFlat);
        baseFlat = "$ " + String.format(Locale.ROOT,"%,.0f", flatPrice);
        flatText.setText(baseFlat);
    }

    public void copyText(){
        //TODO: Remake this with all format somehow

        double[] finals = calculateTotal();

        //out += "================================\n";
        String temp = "";


        if(fb+hb+hs > 0){
            temp += pricesFieldText.getText().toString();
        }

        if(extras.size() > 0){
            temp += "EXTRA CHARGES===================\n";
            temp += "================================\n";

            for(int i = 0; i < extras.size(); i++){
                double curPrice = extrasPrices.get(i);
                String out;

                if(curPrice < 0){out = extras.get(i) + ":   -$ " + String.format(Locale.ROOT, "%,6.2f" ,curPrice) + "\n";}
                else{out = extras.get(i) + ":   $ " + String.format(Locale.ROOT, "%,6.2f" ,curPrice) + "\n";}
                temp += String.format(Locale.ROOT, "%33s", out);
            }

            temp += "\n";
        }

        temp += "FINAL PRICE=======================\n";
        temp += "================================\n";
        temp += String.format(Locale.ROOT, "SUB:                 $ %,.2f" ,finals[0] + finals[1]) + "\n";
        temp += String.format(Locale.ROOT, "                                      -$ %,.2f" ,finals[0]) + "dsc\n";
        temp += String.format(Locale.ROOT, "FIN:                  $ %,.2f" ,finals[1]) + "\n";


        ClipboardManager cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData out = ClipData.newPlainText("output", temp);
        cb.setPrimaryClip(out);

        Toast.makeText(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
    }

    public void toggleShadeContainer(){
        boolean temp = View.VISIBLE != shdCtn.getVisibility();

        if(temp){
            shdCtn.setVisibility(View.VISIBLE);
        }else{
            shdCtn.setVisibility(View.INVISIBLE);
        }
    }

    public void toggleShade(int shadeVal){
        shadeArray[shadeVal].setImageDrawable(ResourcesCompat.getDrawable(this.getResources(), android.R.drawable.btn_star_big_on, null));
        shadeActive[shadeVal] = true;

        updateBasePriceFlats(prices[shadeVal], flatPrice);

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

    public void updateExtrasList(){
        extrasList.removeViewsInLayout(0, extrasList.getChildCount());

        for(int i = 0; i < extras.size(); i++){
            Resources r = getResources();

            ContextThemeWrapper buttonColor;

            if(i == toEdit && readyToDelete) {
                buttonColor = new ContextThemeWrapper(this, R.style.MainActivity_RedderButton);
            }else if(i == toEdit){
                buttonColor = new ContextThemeWrapper(this, R.style.MainActivity_RedButton);
            }else{buttonColor = new ContextThemeWrapper(this, R.style.MainActivity_priceButton);}

            Button temp = new Button(buttonColor);

            double curPrice = extrasPrices.get(i);
            String text;

            if(curPrice < 0){text = extras.get(i) + ":   -$ " + String.format(Locale.ROOT, "%,6.2f" ,Math.abs(curPrice)) + "           ";}
            else{text = extras.get(i) + ":   $ " + String.format(Locale.ROOT, "%,6.2f" ,curPrice) + "           ";}


            temp.setText(text);
            temp.setTransformationMethod(null);
            temp.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

            if(i == toEdit && readyToDelete){
                temp.setBackgroundColor(Color.parseColor("#E33535"));
            }else if(i == toEdit){
                temp.setBackgroundColor(Color.parseColor("#CF7C7C"));
            }else{temp.setBackgroundColor(Color.parseColor("#FF4F4561"));}


            float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20f,r.getDisplayMetrics());
            temp.setHeight((int)height);
            temp.setMinimumHeight((int)height);
            temp.setPadding(0,0,0,0);


            float margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5f,r.getDisplayMetrics());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.setMargins(0,(int)margin,0,0);

            temp.setLayoutParams(param);

            final int val = i;

            temp.setOnClickListener(view -> extraEdit(val));

            extrasList.addView(temp);
        }

        calculateTotal();
    }

    public double[] calculateTotal(){
        double finalPrice = preExtraFinal;
        double finalDiscTot = totalDiscount;

        for(double x : extrasPrices){
            finalPrice += x;
            if(x<0){
                finalDiscTot -= x;
            }
        }

        String temp = "Dsc: -$ " + String.format(Locale.ROOT, "%,.2f" ,finalDiscTot);
        finalDiscText.setText(temp);
        temp = "Tot: $ " + String.format(Locale.ROOT, "%,.2f" ,finalPrice);
        finalPriceText.setText(temp);

        return new double[]{finalDiscTot, finalPrice};
    }


    @Override
    public void onBackPressed() {
        if(toEdit > -1) {
            toEdit = -1;
            readyToDelete = false;
            updateExtrasList();
        }

        if(shdCtn.getVisibility() == View.VISIBLE){
            shdCtn.setVisibility(View.INVISIBLE);
        }else if(extrasInputCtn.getVisibility() == View.VISIBLE){
            extrasInputCtn.setVisibility(View.INVISIBLE);
        }else{
            super.onBackPressed();
        }
    }

    public void extraEdit(int id){
        if(readyToDelete && toEdit == id){
            extras.remove(toEdit);
            extrasPrices.remove(toEdit);

            extrasInputCtn.setVisibility(View.INVISIBLE);

            readyToDelete = false;
            toEdit = -1;

            Toast.makeText(MainActivity.this, ("BALETED!"), Toast.LENGTH_SHORT).show();
        }else if(toEdit > -1 && toEdit == id){
            readyToDelete = true;

        }else{
            toEdit = id;
            readyToDelete = false;

            extrasInputCtn.setVisibility(View.VISIBLE);
            nameInput.requestFocus();

            String temp = ""+extrasPrices.get(id);
            extraPriceInput.setText(temp);
            nameInput.setText(extras.get(id));
        }

        updateExtrasList();
    }
}