package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "CalcErrTag";     // error tag
    private final String KEY = "CalcData";      // key for the shared preferences
    private SharedPreferences sharedPref;       // shared preferences

    Button btnC, btnDel, btnRes;
    TextView txtRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialization
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        Init();

        // Read saved data.
        txtRes.setText(ReadData());
    }

    // Initialization
    void Init() {
        txtRes = findViewById(R.id.txtData);

        btnRes = findViewById(R.id.btnEuqal);
        btnC = findViewById(R.id.btnC);
        btnDel = findViewById(R.id.btnDel);
    }

    // Print each character.
    public void OnCalcButtonClick(View v) {
        // Erase error message.
        if (txtRes.getText().toString().startsWith("ERROR")) {
            txtRes.setTextColor(getResources().getColor(R.color.pureBlack));
            txtRes.setText("");
        }

        try {
            Button btnFunc = (Button) v;
            boolean isFunc = false;     // check if the input character was an operator

            if (btnFunc.getText().toString().equals("*") || btnFunc.getText().toString().equals("/") || btnFunc.getText().toString().equals("-") || btnFunc.getText().toString().equals("+"))
                isFunc = true;

            txtRes.setText(txtRes.getText().toString() + (isFunc ? " " : "") + btnFunc.getText().toString() + (isFunc ? " " : ""));

            // Calculate and save data.
            if (isFunc)
                txtRes.setText(Calculate(txtRes.getText().toString()));

            if (!txtRes.getText().toString().startsWith("ERROR"))
                SaveData(txtRes.getText().toString());
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
    // Remove one character.
    public void OnDelButtonClick(View v){
        if (txtRes.getText().toString().startsWith("ERROR"))
            return;

        try{
            if (txtRes.getText().toString().isEmpty())
                throw new Exception("there is nothing to erase!");

            txtRes.setText(txtRes.getText().toString().substring(0, txtRes.getText().toString().length() - 2));
            SaveData(txtRes.getText().toString());
        } catch (Exception ex) {
            txtRes.setTextColor(getResources().getColor(R.color.pCoral));
            txtRes.setText("ERROR: " + ex.getMessage());
        }
    }
    // Clear the input.
    public void OnCButtonClick(View v){
        txtRes.setText("");
        SaveData(txtRes.getText().toString());
    }

    // Calculate the result.
    public void OnEuqalButtonClick(View v){
        if (txtRes.getText().toString().startsWith("ERROR"))
            return;

        txtRes.setText(Calculate(txtRes.getText().toString()));
        if (!txtRes.getText().toString().startsWith("ERROR"))
            SaveData(txtRes.getText().toString());
    }
    private String Calculate(String str) {
        String res = "";

        String[] arr = str.split(" ");
        if (arr.length%2 == 0)
            return str;

        for (String item: arr) {

        }

        return res;
    }

    // Save data to the shared preferences.
    void SaveData(String data) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY, data);
        editor.apply();
    }
    // Read data from the shared preferences.
    String ReadData() {
        return sharedPref.getString(KEY, "");
    }
}
