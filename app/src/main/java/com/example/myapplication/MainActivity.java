package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int cardBin;
    private  ArrayList<String> historyRequests = new ArrayList<>();
    SharedPreferences shared;
    ArrayAdapter<String> stringArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button aboutButton = findViewById(R.id.about_button);
        Button clearHistory = findViewById(R.id.clear_history_button);
        //Button infoButton = findViewById(R.id.info_button);
        EditText editText = findViewById(R.id.editTextNumberSigned);
        ListView historyList = findViewById(R.id.historyListView);


        shared = getSharedPreferences("Request_history", MODE_PRIVATE);


        stringArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, historyRequests);
        historyList.setAdapter(stringArrayAdapter);

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                TextView textView = (TextView) itemClicked;
                String strText = textView.getText().toString(); // получаем текст нажатого элемента

                cardBin = Integer.parseInt(strText);
                Intent intent = new Intent(MainActivity.this, PlainActivity.class);
                intent.putExtra("cardBIN", cardBin);
                startActivity(intent);

            }
        });

        findViewById(R.id.about_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                    }
                });

        findViewById(R.id.clear_history_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               historyRequests.clear();
               stringArrayAdapter.notifyDataSetChanged();
            }
        });
//
//        findViewById(R.id.info_button).setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
//                        startActivity(intent);
//                    }
//                });

        editText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        String temp = editText.getText().toString();
                        if (!temp.equals("")) {
                            historyRequests.add(temp);
                            stringArrayAdapter.notifyDataSetChanged();
                            cardBin = Integer.parseInt(temp);
//                            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                            Intent intent = new Intent(MainActivity.this, PlainActivity.class);
                            intent.putExtra("cardBIN", cardBin);
                            saveSharedPreferences();
                            editText.setText("");
                            startActivity(intent);
                            return true;
                        }
                    }
                    return false;
                }
            }
        );




    }

    @Override
    public void onClick(View view) {
    }



    private void saveSharedPreferences() {
        SharedPreferences.Editor editor = shared.edit();
        Set<String> set = new HashSet<>();
        set.addAll(historyRequests);
        editor.putStringSet("DATE_LIST", set);
        editor.apply();
        Log.d("storesharedPreferences",""+set);
    }

    private void retrieveSharedValue() {
        Set<String> set = shared.getStringSet("DATE_LIST", null);
        historyRequests.clear();
        historyRequests.addAll(set);
        Log.d("saveSharedPreferences"," " + set);
    }


}