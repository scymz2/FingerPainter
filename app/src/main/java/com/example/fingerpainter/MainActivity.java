package com.example.fingerpainter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    public static final int COLOR_REQUEST = 1;
    public static final int BRUSH_REQUEST = 2;
    private static final int STORAGE_PERMISSION_REQUEST_WRITE = 3;

    // UI components
    private FingerPainterView fpv;
    private Button c_selection, b_selection, save;
    private ImageView brush_shape;
    private TextView color_text, brush_text;

    // The variables for the brush
    private int brush_color, brush_width;
    private Paint.Cap brush_type;

    // filename
    private String FILE_NAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        // initialize the FPV variables
        brush_color = fpv.getColour();
        brush_width = fpv.getBrushWidth();
        brush_type = fpv.getBrush();

        // Implicit intent
        fpv.load(getIntent().getData());

        // Load settings if state saved
        if(savedInstanceState != null)
        {
            Log.d(TAG, "onCreate: Load savedInstance");
            brush_color = savedInstanceState.getInt("color");
            brush_type = (Paint.Cap)savedInstanceState.getSerializable("type");
            brush_width = savedInstanceState.getInt("width");

            // Set finger painter view settings
            Log.d(TAG, "onCreate: " + String.valueOf(brush_width));
            fpv.setBrush(brush_type);
            fpv.setBrushWidth(brush_width);
            fpv.setColour(brush_color);

            // Change UI
            setBrush(brush_type, brush_width);
            setColor(brush_color);
        }


    }


    // Store the data of the activity before the screen changes direction
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.d(TAG, "onSaveInstanceState: onSaveInstanceState");

        // Store the activity variables
        state.putInt("color", brush_color);
        state.putInt("width", brush_width);
        state.putSerializable("type", brush_type);
    }

    // Initialize the View
    private void initView() {
        // Reference
        fpv = findViewById(R.id.fpview);
        c_selection = findViewById(R.id.color);
        b_selection = findViewById(R.id.brush);
        color_text = findViewById(R.id.color_text);
        brush_text = findViewById(R.id.brush_text);
        brush_shape = findViewById(R.id.brush_box);
        save = findViewById(R.id.save);

        // Set onclickListener
        save.setOnClickListener(new ButtonListener());
        c_selection.setOnClickListener(new ButtonListener());
        b_selection.setOnClickListener(new ButtonListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COLOR_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String color = bundle.getString("color");

                // Set the color of the brush and modify the UI
                brush_color = Color.parseColor(color);
                color_text.setText(color);
                setColor(brush_color);

                Toast.makeText(MainActivity.this,  color + " SELECTED!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "NONE COLOR SELECTED!", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == BRUSH_REQUEST){
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                brush_type = (Paint.Cap) bundle.getSerializable("type");

                // Set the width of the brush
                brush_width = bundle.getInt("width");
                setBrush(brush_type, brush_width); //set the UI of the brush box

                Toast.makeText(MainActivity.this, "BRUSH TYPE: "+ String.valueOf(brush_type)  + " WIDTH: " + String.valueOf(brush_width), Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "NONE BRUSH SET!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Handle results of permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if(requestCode == STORAGE_PERMISSION_REQUEST_WRITE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                fpv.saveBitmap(FILE_NAME);
            else if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this,"Please enable storage permissions for image saving!",Toast.LENGTH_SHORT).show();

        }
    }

    // The class used for button listener
    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.color:
                    Log.d(TAG, "onClick: select color");
                    Intent color = new Intent(MainActivity.this,ColorActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(color, COLOR_REQUEST);
                    break;
                case R.id.brush:
                    Log.d(TAG, "onClick: select brush");
                    Bundle bundle = new Bundle();
                    bundle.putInt("width", brush_width);
                    bundle.putSerializable("type", brush_type);
                    Intent brush = new Intent(MainActivity.this,BrushActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    brush.putExtras(bundle);
                    startActivityForResult(brush, BRUSH_REQUEST);
                    break;
                case R.id.save:
                    Log.d(TAG, "onClick: select save");
                    final EditText file_name = new EditText(MainActivity.this);
                    new AlertDialog.Builder(MainActivity.this).setTitle("PLEASE SPECIFY THE FILE NAME HERE::")
                            .setView(file_name).setPositiveButton("ok!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FILE_NAME = file_name.getText().toString();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if(!checkFileName(FILE_NAME)){
                                    Toast.makeText(MainActivity.this, "save failed! invalid filename!", Toast.LENGTH_SHORT).show();
                                }
                                else if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                                    fpv.saveBitmap(FILE_NAME);
                                }else{
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_WRITE);
                                }
                            }
                        }
                    }).setNegativeButton("cancel", null).show();

            }
        }
    }

    // Set the UI to show the current brush color
    private void setColor(int color_value){
        color_text.setTextColor(color_value);
        fpv.setColour(color_value);
        brush_shape.setColorFilter(color_value);
    }

    // Set the UI to show the current brush shape and width
    private void setBrush(Paint.Cap type, int width){
        brush_text.setText(String.valueOf(width) + "DP");
        fpv.setBrushWidth(width);
        switch (type){
            case ROUND:
                brush_shape.setImageResource(R.drawable.circle);
                fpv.setBrush(Paint.Cap.ROUND);
                break;
            case SQUARE:
                brush_shape.setImageResource(R.drawable.square);
                fpv.setBrush(Paint.Cap.SQUARE);
                break;
            case BUTT:
                brush_shape.setImageResource(R.drawable.rectangle);
                fpv.setBrush(Paint.Cap.BUTT);
                break;
        }
    }

    // Check if the filename is valid
    public boolean checkFileName(String fileName){
        if (fileName == null || fileName.length() > 255)
            return false;
        else
            return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
    }

}