package com.example.comissioncalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class ConfigMenu extends AppCompatActivity {

    public Button back;
    public Button applyBack;

    public SharedPreferences prefs;
    public InputMethodManager imm;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_menu);

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

    public void applyConfig(){

    }

    public void applyBack(){
        applyConfig();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void updateFields()  {

    }
}