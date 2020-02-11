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
    private float _firstOperand, _secondOperand;

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

            // Calculate data.
            String res = Calculate(btnFunc.getText().toString());
            // Save data if there is no errors.
            if (!txtRes.getText().toString().startsWith("ERROR")) {
                txtRes.setText(txtRes.getText().toString() + res);
                SaveData(txtRes.getText().toString());
            }
            else
                txtRes.setTextColor(getResources().getColor(R.color.pCoral));
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

            txtRes.setText(Calculate(txtRes.getText().toString().substring(0, txtRes.getText().toString().length() - 1)));
            SaveData(txtRes.getText().toString());
        } catch (Exception ex) {
            txtRes.setTextColor(getResources().getColor(R.color.pCoral));
            txtRes.setText("ERROR: " + ex.getMessage());
        }
    }
    // Clear the input.
    public void OnCButtonClick(View v){
        txtRes.setText(Calculate(""));
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
        // Check to see if the input is empty.
        if (!str.contains(" "))
            return str;

        String res = "";
        String[] arr = str.split(" ");

        // Test the input.
        if (arr[0].startsWith(".") || arr[0].equals("*") || arr[0].equals("+") || arr[0].equals("-") || arr[0].equals("/"))
            return "ERROR: Wrong input!";
        // Get the first operand.
        try {
            _firstOperand = Float.parseFloat(arr[0]);
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
        // Test if the calculation can be performed (the input contains both operands).
        if (arr.length % 2 == 0)
            return str;
        // Get the second operand and perform the calculations.
        try {
            _secondOperand = Float.parseFloat(arr[2]);
            switch (arr[1]) {
                case "*":
                    res = Float.toString(_firstOperand * _secondOperand);
                    break;

                case "/":
                    res = Float.toString(_firstOperand / _secondOperand);
                    break;

                case "+":
                    res = Float.toString(_firstOperand + _secondOperand);
                    break;

                case "-":
                    res = Float.toString(_firstOperand - _secondOperand);
                    break;
            }
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
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
