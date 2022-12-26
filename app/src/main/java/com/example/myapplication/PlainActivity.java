package com.example.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Entity.Entity;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class PlainActivity extends AppCompatActivity {

    private final static String URL = "https://lookup.binlist.net/";
    ProgressDialog pd;
    TextView bankUrView;
    TextView bankPhoneView;
    TextView plainTextView;
    private String bankUrl;
    private String bankPhone;

    private int cardBin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plain);
        plainTextView = findViewById(R.id.plainText);
        bankUrView = findViewById(R.id.bankUrText);
        bankPhoneView = findViewById(R.id.bankPhoneText);

        Log.d("MyLog: ", "--Info Activity onCreate--");
        Intent intent = getIntent();
        cardBin = intent.getIntExtra("cardBIN", cardBin);
        Log.d("MyLog: ", " get card BIN from intent: " + cardBin);

        //stub
        //cardBin = 45717360;

        new JsonTask().execute(URL);

    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(PlainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(URL + cardBin);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("MyLog: ", "Response: -> " + line);
                }

                return buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }

            String parsedJson = parseJSON(result);

            plainTextView.setText(parsedJson);
            bankUrView.setText(bankUrl);
            bankPhoneView.setText(bankPhone);
        }
    }


    private String parseJSON(String data) {
        String result = "Data not available";
        Entity entity;
        try {
            Gson g = new Gson();
            entity = g.fromJson(data, Entity.class);

            Log.d("-----> ENTITY", entity.toString());
            Log.d("-----> ", entity.getNumber().toString());
            Log.d("-----> ", entity.getScheme());
            Log.d("-----> ", entity.getType());
            Log.d("-----> ", entity.getBrand());
            Log.d("-----> ", String.valueOf(entity.getPrepaid()));
            Log.d("-----> ", entity.getBank().toString());
            Log.d("-----> ", entity.getCountry().toString());

            result = "CARD INFO: " + '\n' +
                    entity.getNumber().toString() + '\n' +
                    " SCHEME / NETWORK: " + entity.getScheme() + '\n' +
                    "TYPE: " + entity.getType() + '\n' +
                    "BRAND: " + entity.getBrand() + '\n' +
                    "PREPAID: " + entity.getPrepaid() + '\n' +
                    entity.getCountry() + '\n' +
                    entity.getBank();

            bankUrl = entity.getBank().getUrl();
            bankPhone = entity.getBank().getPhone();

        } catch (Exception e) {
            Log.i("App", "-----------------Error parsing data-----------------" + e.getMessage());
            Toast.makeText(PlainActivity.this, "Failed to load data", Toast.LENGTH_LONG).show();
        }

        return result;
    }


}