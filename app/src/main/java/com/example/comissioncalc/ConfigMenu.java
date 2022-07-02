package com.example.comissioncalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;

public class ConfigMenu extends AppCompatActivity {

    public Button back;
    public Button applyBack;

    public SharedPreferences prefs;
    public InputMethodManager imm;

    //editText Declarations
    public EditText baseText;
    public EditText flatAddText;

    public EditText celText;
    public EditText flatsText;
    public EditText linesText;
    public EditText sketchText;
    public EditText tmgText;

    public EditText dscP1Text;
    public EditText dcsPerText;

    public EditText hbNumerText;
    public EditText hbDenomText;

    public EditText hsNumerText;
    public EditText hsDenomText;

    //internal declarations
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

    //Collection Declarations
    public EditText[] configs;
    public String[] configNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_menu);

        int numSets = 13;

        configs = new EditText[numSets];
        configNames = new String[]{"BasePrice", "FlatPrice", "Cel", "Flats", "Lines", "Sketch", "TGM", "DscP1", "DscPer", "hbNumer", "hbDenom", "hsNumer", "hsDenom"};

        //editText declarations==============================
        configs[0] = baseText = findViewById(R.id.basePriceInp);
        configs[1] = flatAddText = findViewById(R.id.flatPriceInp);

        configs[2] = celText = findViewById(R.id.celInp);
        configs[3] = flatsText = findViewById(R.id.flatsInp);
        configs[4] = linesText = findViewById(R.id.linesInp);
        configs[5] = sketchText = findViewById(R.id.sketchInp);
        configs[6] = tmgText = findViewById(R.id.tgmInp);

        configs[7] = dscP1Text = findViewById(R.id.dscP1Inp);
        configs[8] = dcsPerText = findViewById(R.id.dscPerInp);

        configs[9] = hbNumerText = findViewById(R.id.hbNumer);
        configs[10] = hbDenomText = findViewById(R.id.hbDenom);

        configs[11] = hsNumerText = findViewById(R.id.hsNumer);
        configs[12] = hsDenomText = findViewById(R.id.hsDenom);

        //activity declarations==============================
        back = findViewById(R.id.BackBtn);
        applyBack = findViewById(R.id.backApplyBtn);

        prefs = getSharedPreferences("Prefs", 0);
        checkPrefs();

        //internal declarations==============================
        loadPrefs();
        updateFields();

        //do stuff===========================================
        back.setOnClickListener(view -> onBackPressed());
        applyBack.setOnClickListener(view -> applyBack());

        //TODO: declare onclick and onfocus for all the edittexts that clears. Also make them not chain to prevent breaking.
        //TODO: Implement action.done for all fields that re-sets the stored values to a null value


    }

    public void checkPrefs(){
        SharedPreferences.Editor edit = prefs.edit();
        boolean change = false;

        if(!prefs.contains("BasePrice")){edit.putLong("BasePrice", Double.doubleToRawLongBits(90.0));change = true;}
        if(!prefs.contains("FlatPrice")){edit.putLong("FlatPrice", Double.doubleToRawLongBits(00.0));change = true;}

        if(!prefs.contains("Cel")){edit.putLong("Cel", Double.doubleToRawLongBits(80.0));change = true;}
        if(!prefs.contains("Flats")){edit.putLong("Flats", Double.doubleToRawLongBits(75.0));change = true;}
        if(!prefs.contains("Lines")){edit.putLong("Lines", Double.doubleToRawLongBits(65.0));change = true;}
        if(!prefs.contains("Sketch")){edit.putLong("Sketch", Double.doubleToRawLongBits(55.0));change = true;}
        if(!prefs.contains("TGM")){edit.putLong("TGM", Double.doubleToRawLongBits(50.0));change = true;}

        if(!prefs.contains("DscP1")){edit.putInt("DscP1", 2);change = true;}
        if(!prefs.contains("DscPer")){edit.putInt("DscPer", 50);change = true;}

        if(!prefs.contains("hbNumer")){edit.putInt("hbNumer", 2);change = true;}
        if(!prefs.contains("hbDenom")){edit.putInt("hbDenom", 3);change = true;}

        if(!prefs.contains("hsNumer")){edit.putInt("hsNumer", 1);change = true;}
        if(!prefs.contains("hsDenom")){edit.putInt("hsDenom", 2);change = true;}

        if(change){
            edit.commit();
        }
    }

    public void loadPrefs(){
        basePrice = shaded = Double.longBitsToDouble(prefs.getLong("BasePrice", Double.doubleToRawLongBits(90.0)));
        flatPrice = Double.longBitsToDouble(prefs.getLong("FlatPrice", Double.doubleToRawLongBits(00.0)));

        cel = Double.longBitsToDouble(prefs.getLong("Cel", Double.doubleToRawLongBits(80.0)));
        flats = Double.longBitsToDouble(prefs.getLong("Flats", Double.doubleToRawLongBits(75.0)));
        lines = Double.longBitsToDouble(prefs.getLong("Lines", Double.doubleToRawLongBits(65.0)));
        sketch = Double.longBitsToDouble(prefs.getLong("Sketch", Double.doubleToRawLongBits(55.0)));
        tgmStick = Double.longBitsToDouble(prefs.getLong("TGM", Double.doubleToRawLongBits(50.0)));

        discountPer1 = prefs.getInt("DscP1", 2);
        discountPercent = prefs.getInt("DscPer", 50);

        hbNumer = prefs.getInt("hbNumer", 2);
        hbDenom = prefs.getInt("hbDenom", 3);

        hsNumer = prefs.getInt("hsNumer", 1);
        hsDenom = prefs.getInt("hsDenom", 2);
    }

    public double validDoubleInput(EditText checkText){
        String check = checkText.getText().toString();

        if(!"".equals(check) && !".".equals(check) && !"-".equals(check)) {
            double price = (double)Math.round(100 * Double.parseDouble(check))/100.0;

            //insert checks here if need be.

            return price;
        }

        return Double.MIN_VALUE;
    }

    public int validIntInput(EditText checkText){
        String check = checkText.getText().toString();

        if(!"".equals(check) && !"-".equals(check)) {
            int price = Integer.parseInt(check);

            //insert checks here if need be.

            return price;
        }

        return Integer.MIN_VALUE;
    }

    public boolean validCheck(double val){
        if(val == Double.MIN_VALUE){return false;}
        return true;
    }

    public boolean validCheck(int val){
        if(val == Integer.MIN_VALUE){return false;}
        return true;
    }

    public boolean applyConfig(){
        SharedPreferences.Editor edit = prefs.edit();

        for(int i = 0; i < configs.length; i++){
            if(configs[i].getInputType() == (InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER)){
                double tempDouble = validDoubleInput(configs[i]);;

                if(validCheck(tempDouble)){edit.putLong(configNames[i], Double.doubleToRawLongBits(tempDouble));}
                else{
                    String error = "Invalid Value Entered For " + configNames[i];
                    Toast.makeText(ConfigMenu.this, error , Toast.LENGTH_SHORT).show();
                    return false;
                }

            }else if(configs[i].getInputType() == InputType.TYPE_CLASS_NUMBER){
                int tempInt = validIntInput(configs[i]);

                if(validCheck(tempInt)){edit.putInt(configNames[i], tempInt);}
                else{
                    String error = "Invalid Value Entered For " + configNames[i];
                    Toast.makeText(ConfigMenu.this, error , Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        edit.commit();

        return true;
    }

    //for future apply button
    public void apply(){
        if(!applyConfig()){return;}
        loadPrefs();
        updateFields();
    }

    public void applyBack(){
        //if it encounters an error it won't go back.
        //TODO: Maybe make this force all values to be the originals again instead of stopping going back.
        if(!applyConfig()){return;}
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        //TODO: make hitting back while in a text field repopulate it with the current value
        super.onBackPressed();
    }

    public void updateFields()  {
        String temp;

        temp = "" + basePrice;
        baseText.setText(temp);

        temp = "" + flatPrice;
        flatAddText.setText(temp);

        temp = "" + cel;
        celText.setText(temp);

        temp = "" + flats;
        flatsText.setText(temp);

        temp = "" + lines;
        linesText.setText(temp);

        temp = "" + sketch;
        sketchText.setText(temp);

        temp = "" + tgmStick;
        tmgText.setText(temp);

        temp = "" + discountPer1;
        dscP1Text.setText(temp);

        temp = "" + discountPercent;
        dcsPerText.setText(temp);

        temp = "" + hbNumer;
        hbNumerText.setText(temp);

        temp = "" + hbDenom;
        hbDenomText.setText(temp);

        temp = "" + hsNumer;
        hsNumerText.setText(temp);

        temp = "" + hsDenom;
        hsDenomText.setText(temp);
    }
}