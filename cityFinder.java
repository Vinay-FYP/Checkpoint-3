package com.example.weatherappattempt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class cityFinder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_finder);

        final EditText search = findViewById(R.id.searchCity);
        ImageView  backbutton = findViewById(R.id.backbtn);

        //listner
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newCity = search.getText().toString();
                Intent i = new Intent(cityFinder.this, MainActivity.class);
                i.putExtra("City",newCity);
                startActivity(i);

                return false;
            }
        });
    }
}