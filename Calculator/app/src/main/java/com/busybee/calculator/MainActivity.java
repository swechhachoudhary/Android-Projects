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

public class MainActivity extends AppCompatActivity {

    private EditText resultText;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;

    private Double operand1 = null;
    private Double operand2 = null;
    private Double ans = null;
    private String previousOperation = "=";
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = (EditText) findViewById(R.id.resultText);

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter();

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            }
        });

    }
//        button0 = (Button) findViewById(R.id.button0);
//        button1 = (Button) findViewById(R.id.button1);
//        button2 = (Button) findViewById(R.id.button2);
//        button3 = (Button) findViewById(R.id.button3);
//
//        View.OnClickListener clickListernerDigit = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Button button = (Button) view;
//                temp = resultText.getText().toString();
//                resultText.setText(temp + button.getText().toString());
//                Log.d("resultText swechha: ", resultText.getText().toString());
//                if (previousOperation.equals("=")){
//                    operand1 = Double.valueOf(resultText.getText().toString());
//                }
//                else{
//                    if (operand2 != null){
//                        operand2 = Double.valueOf(operand2.toString() + button.getText().toString());
//                    }
//                    else{
//                        operand2 = Double.valueOf(button.getText().toString());
//                    }
//                }
//            }
//        };
//
//        button0.setOnClickListener(clickListernerDigit);
//        button1.setOnClickListener(clickListernerDigit);
//        button2.setOnClickListener(clickListernerDigit);
//
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Button button = (Button) view;
//                String operation;
//                if(previousOperation.equals("=")){
//                    previousOperation = button.getText().toString();
//                    temp = resultText.getText().toString();
//                    resultText.setText(temp + button.getText());
//                }
//                else {
//                    if (operand2 != null){
//                        ans = performOperation(operand1,operand2,previousOperation);
//                        operand1 = ans;
//                        previousOperation = button.getText().toString();
//                        resultText.setText(operand1.toString()+previousOperation);
//                    }
//
//                }
//            }
//        });
//
//    }

    public Double performOperation(double operand1, double operand2, String operation){
        switch (operation){
            case "+":
                ans = operand1 + operand2;
                break;
            case "-":
                ans = operand1 - operand2;
                break;
            case "*":
                ans = operand1 * operand2;
                break;
            case "/":
                ans = operand1/operand2;
                break;
        }
        return ans;
    }
}
