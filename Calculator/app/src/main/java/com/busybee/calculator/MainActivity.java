package com.busybee.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;     // "/" operation button
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;     // "*" operation button
    private Button button8;
    private Button button9;
    private Button button10;
    private Button button11;    // "-" operation button
    private Button button12;
    private Button button13;    // "." operation button
    private Button button14;    // "=" operation button
    private Button button15;    // "+" operation button

    private Button deleteButton;
  
    private Double operand1 = null;
    private Double operand2 = null;
    private Double ans = null;
    private String previousOperation = "=";
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = (TextView) findViewById(R.id.resultText);
        deleteButton = (Button) findViewById(R.id.delete);

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new GridAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                switch (position){
                    case 0:
                        digitOrDotClickListerner(v);
                        break;
                    case 1:
                        digitOrDotClickListerner(v);
                        break;
                    case 2:
                        digitOrDotClickListerner(v);
                        break;
                    case 3:
                        operationButtonClickListerner(v);
                        break;
                    case 4:
                        digitOrDotClickListerner(v);
                        break;
                    case 5:
                        digitOrDotClickListerner(v);
                        break;
                    case 6:
                        digitOrDotClickListerner(v);
                        break;
                    case 7:
                        operationButtonClickListerner(v);
                        break;
                    case 8:
                        digitOrDotClickListerner(v);
                        break;
                    case 9:
                        digitOrDotClickListerner(v);
                        break;
                    case 10:
                        digitOrDotClickListerner(v);
                        break;
                    case 11:
                        operationButtonClickListerner(v);
                        break;
                    case 12:
                        digitOrDotClickListerner(v);
                        break;
                    case 13:
                        break;
                    case 14:
                        operationButtonClickListerner(v);
                        break;
                    case 15:
                        operationButtonClickListerner(v);
                        break;
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultText.setText("");
                previousOperation = "=";
                operand1 = null;
                operand2 = null;
            }
        });
    }

    public void digitOrDotClickListerner(View view){
        Button button = (Button) view;
        temp = resultText.getText().toString();
        resultText.setText(temp + button.getText().toString());
        Log.d("resultText : ", resultText.getText().toString());
        if (previousOperation.equals("=")){
            operand1 = Double.valueOf(resultText.getText().toString());
        }
        else{
            if (operand2 != null){
                operand2 = Double.valueOf(operand2.toString() + button.getText().toString());
            }
            else{
                operand2 = Double.valueOf(button.getText().toString());
            }
        }
    }

    public void operationButtonClickListerner(View view){
        Button button = (Button) view;
        if(operand1 == null && operand2 == null){
            Toast.makeText(getApplicationContext(),"Not a valid operation.",Toast.LENGTH_LONG).show();
            return;
        }
        else if (operand1 == null){
            Toast.makeText(getApplicationContext(),"Not a valid operation.",Toast.LENGTH_LONG).show();
            return;
        }
        else if (operand2 != null){
            ans = performOperation(operand1,operand2,previousOperation);
            operand1 = ans;
            operand2 = null;
            previousOperation = button.getText().toString();
            if (!previousOperation.equals("=")){
                resultText.setText(operand1.toString()+previousOperation);
            }
            else{
                resultText.setText(operand1.toString());
            }

        }
        else if(operand2 == null && previousOperation.equals("=")){
            if (!button.getText().toString().equals("=")) {
                previousOperation = button.getText().toString();
                temp = resultText.getText().toString();
                resultText.setText(temp + button.getText().toString());
            }
            else{
                Toast.makeText(getApplicationContext(),"Not a valid operation.",Toast.LENGTH_LONG).show();
            }
        }
        else if (operand2 == null && !previousOperation.equals("=")){
            Toast.makeText(getApplicationContext(),"Not a valid operation.",Toast.LENGTH_LONG).show();
        }
    }

    public Double performOperation(double operand1, double operand2, String operation){
        switch (operation){
            case "+":
                ans = operand1 + operand2;
                Log.d("ADD : ","Done");
                break;
            case "-":
                ans = operand1 - operand2;
                Log.d("subtract : ","Done");
                break;
            case "*":
                ans = operand1 * operand2;
                Log.d("multiply : ","Done");
                break;
            case "/":
                ans = operand1/operand2;
                Log.d("Division : ","Done");
                break;
        }
        return ans;
    }
}
