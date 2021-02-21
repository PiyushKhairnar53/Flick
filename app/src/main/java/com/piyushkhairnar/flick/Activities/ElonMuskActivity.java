package com.piyushkhairnar.flick.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.piyushkhairnar.flick.R;

public class ElonMuskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elon_musk);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}