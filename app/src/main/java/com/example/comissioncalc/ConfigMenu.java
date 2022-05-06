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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_menu);

        //activity declarations==============================
        back = findViewById(R.id.BackBtn);
        applyBack = findViewById(R.id.backApplyBtn);

        //internal declarations==============================

        //do stuff===========================================
        back.setOnClickListener(view -> onBackPressed());
        applyBack.setOnClickListener(view -> applyBack());
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
}