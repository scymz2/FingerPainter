package com.example.fingerpainter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ColorActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    private CardView orange;
    private CardView yellow;
    private CardView light_yellow;
    private CardView green;
    private CardView median_green;
    private CardView light_blue;
    private CardView blue;
    private CardView purple;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        
        initView();
    }

    // Reference the UI components
    private void initView(){
        orange = findViewById(R.id.orange);
        yellow = findViewById(R.id.yellow);
        light_yellow = findViewById(R.id.light_yellow);
        green = findViewById(R.id.green);
        median_green = findViewById(R.id.median_green);
        light_blue = findViewById(R.id.light_blue);
        blue = findViewById(R.id.blue);
        purple = findViewById(R.id.purple);

        orange.setOnClickListener(new ColorListener());
        yellow.setOnClickListener(new ColorListener());
        light_yellow.setOnClickListener(new ColorListener());
        green.setOnClickListener(new ColorListener());
        median_green.setOnClickListener(new ColorListener());
        light_blue.setOnClickListener(new ColorListener());
        blue.setOnClickListener(new ColorListener());
        purple.setOnClickListener(new ColorListener());


    }

    // Listener for color selection
    class ColorListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            Intent result = new Intent(ColorActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            switch(view.getId()){
                case R.id.orange:
                    Log.d(TAG, "onClick: orange");
                    bundle.putString("color", "#eb4310");
                    break;
                case R.id.yellow:
                    Log.d(TAG, "onClick: yellow");
                    bundle.putString("color", "#f6941d");
                    break;
                case R.id.light_yellow:
                    Log.d(TAG, "onClick: light yellow");
                    bundle.putString("color", "#fff040");
                    break;
                case R.id.green:
                    Log.d(TAG, "onClick: green");
                    bundle.putString("color", "#99cc33");
                    break;
                case R.id.median_green:
                    Log.d(TAG, "onClick: median green");
                    bundle.putString("color", "#239676");
                    break;
                case R.id.light_blue:
                    Log.d(TAG, "onClick: light blue");
                    bundle.putString("color", "#1f9baa");
                    break;
                case R.id.blue:
                    Log.d(TAG, "onClick: blue");
                    bundle.putString("color", "#3366cc");
                    break;
                case R.id.purple:
                    Log.d(TAG, "onClick: purple");
                    bundle.putString("color", "#a1488e");
                    break;
            }
            result.putExtras(bundle);
            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }
}