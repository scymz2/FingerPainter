package com.example.fingerpainter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

public class BrushActivity extends AppCompatActivity{


    private Spinner type_spinner;
    private Spinner width_spinner;
    private ImageView size, type;


    private Paint.Cap brush_type;
    private int brush_width;

    // Index for spinner selection
    private int width_index;
    private int type_index;

    private Button set;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush);

        //receive current brush type and size
        Bundle bundle = getIntent().getExtras();
        brush_type = (Paint.Cap) bundle.getSerializable("type");
        brush_width = bundle.getInt("width");
        getSpinnerSelection(brush_width, brush_type);
        initView();

        if(savedInstanceState != null){
            type_index = savedInstanceState.getInt("type");
            width_index = savedInstanceState.getInt("width");
            type_spinner.setSelection(type_index);
            width_spinner.setSelection(width_index);

            // Set the brush view for user to
            setBrushViewSize(brush_width);
            setBrushViewType(type_index);
        }


    }

    // store the data of the activity before the screen changes orientation
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt("width",width_index);
        state.putInt("type", type_index);

    }


    private void initView(){
        size = findViewById(R.id.b_size);
        type = findViewById(R.id.b_type);

        //spinner one for brush type selection
        type_spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_array,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        type_spinner.setAdapter(adapter);
        type_spinner.setSelection(type_index);
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type_index = i;
                setBrushViewType(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //spinner tow for brush size selection
        width_spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> size_adapter = ArrayAdapter.createFromResource(this, R.array.size_array,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        width_spinner.setAdapter(size_adapter);
        width_spinner.setSelection(width_index);
        width_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                width_index = i;
                brush_width = Integer.valueOf(adapterView.getItemAtPosition(i).toString());
                setBrushViewSize(brush_width);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {



            }
        });

        set = findViewById(R.id.button);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent result = new Intent(BrushActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                bundle.putSerializable("type", brush_type);
                bundle.putInt("width", brush_width);
                result.putExtras(bundle);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });

    }

    private void getSpinnerSelection(int width, Paint.Cap type){
        // get the selection of the width spinner
        switch (width){
            case 5:
                width_index = 0;
                break;
            case 10:
                width_index = 1;
                break;
            case 15:
                width_index = 2;
                break;
            case 20:
                width_index = 3;
                break;
            case 25:
                width_index = 4;
                break;
            case 30:
                width_index = 5;
                break;
        }

        // Set selection of the type spinner
        switch (type){
            case ROUND:
                type_index = 0;
                break;
            case SQUARE:
                type_index = 1;
                break;
            case BUTT:
                type_index = 2;
                break;
        }
    }

    private void setBrushViewSize(int width){
        ViewGroup.LayoutParams params = size.getLayoutParams();
        params.height=width+8;
        params.width =width+8;
        size.setLayoutParams(params);
    }


    private void setBrushViewType(int index){
        switch (type_index){
            case 0:
                type.setImageResource(R.drawable.round);
                size.setImageResource(R.drawable.round);
                brush_type = Paint.Cap.ROUND;
                break;
            case 1:
                type.setImageResource(R.drawable.square);
                size.setImageResource(R.drawable.square);
                brush_type = Paint.Cap.SQUARE;
                break;
            case 2:
                type.setImageResource(R.drawable.rectangle);
                size.setImageResource(R.drawable.rectangle);
                brush_type = Paint.Cap.BUTT;
                break;
        }
    }
}