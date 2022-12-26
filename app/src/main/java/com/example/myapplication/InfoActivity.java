package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Entity.Entity;
import com.example.myapplication.adapter.ExpListAdapterMapImpl;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class InfoActivity extends AppCompatActivity {

    private final static String URL = "https://lookup.binlist.net/";
    ProgressDialog pd;
    TextView txtJson;
    private int cardBin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //txtJson = findViewById(R.id.jsonItem);
        Log.d("MyLog: ", "--Info Activity onCreate--");
        Intent intent = getIntent();
        cardBin = intent.getIntExtra("cardBIN", cardBin);
        Log.d("MyLog: ", " get card BIN from intent: " + cardBin);

        //stub
        //cardBin = 45717360;

         new JsonTask().execute(URL);





    }

    public static boolean isGetter(Method method){
        if(!method.getName().toLowerCase().startsWith("get")) return false;
        if(method.getParameterTypes().length != 0)   return false;
        if(void.class.equals(method.getReturnType())) return false;
        return true;
    }


    private class JsonTask extends AsyncTask<String, String, Entity> {

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(InfoActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected Entity doInBackground(String... params) {
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

                return parseJSON(buffer.toString());

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Entity entity) {
            super.onPostExecute(entity);
            if (pd.isShowing()) {
                pd.dismiss();
            }

            Log.d("MyLog", "--onPostExecute-- ");

            ArrayList<ArrayList<String>> groups = new ArrayList<>();

            ArrayList<String> groupsNames = new ArrayList<>();
            for (Field f : entity.getClass().getDeclaredFields()) {
                groupsNames.add(f.getName());
            }
            Log.d("MyLog ", groupsNames.toString());


            ArrayList<String> numbers = new ArrayList<>();
            for (Field f : entity.getNumber().getClass().getDeclaredFields()) {
                numbers.add(f.getName());
            }
            groups.add(numbers);

            groups.add(new ArrayList<>(Collections.singletonList(entity.getScheme())));

            groups.add(new ArrayList<>(Collections.singletonList(entity.getType())));

            ArrayList<String> bankFields = new ArrayList<>();
            Arrays.stream(entity.getBank().getClass().getDeclaredFields()).forEach(f -> bankFields.add(f.getName()));

            for (Field f : entity.getBank().getClass().getDeclaredFields()) {
                bankFields.add(f.getName());
            }
            groups.add(bankFields);
            groups.add(new ArrayList<>(Collections.singletonList(entity.getBrand())));
            groups.add(new ArrayList<>(Collections.singleton(Boolean.toString(entity.getPrepaid()))));

            ArrayList<String> counties = new ArrayList<>();
            for (Field f : entity.getCountry().getClass().getDeclaredFields()) {
                counties.add(f.getName());
            }
            groups.add(counties);


//            ArrayList<String> children1 = new ArrayList<>();
//            ArrayList<String> children2 = new ArrayList<>();
//            children1.add("Child_1");
//            children1.add("Child_2");
//            groups.add(children1);
//            children2.add("Child_1");
//            children2.add("Child_2");
//            children2.add("Child_3");
//            groups.add(children2);

            ExpandableListView listView = findViewById(R.id.expandableListView);
//            ExpListAdapter adapter = new ExpListAdapter(getApplicationContext(), groups);
            ExpListAdapterMapImpl adapter = new ExpListAdapterMapImpl(getApplicationContext(), groups, groupsNames);
//            MultiSelectExpandableListAdapter adapter = new MultiSelectExpandableListAdapter(getApplicationContext(), groups);
            Log.d("MyLog", "--adapter set--");
            listView.setAdapter(adapter);


            //String parsedJson = parseJSON(result);

            //txtJson.setText(parsedJson);

        }

    }


    private Entity parseJSON(String data) {
        String result = "Data not available";
        Entity entity= new Entity();
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

            result = entity.getNumber().toString() + "\n"
                    + entity.getScheme() + "\n"
                    + entity.getType() + "\n"
                    + entity.getBrand() + "\n"
                    + entity.getPrepaid() + "\n"
                    + entity.getBank().toString() + "\n"
                    + entity.getCountry().toString();

        } catch (Exception e) {
            Log.i("App", "-----------------Error parsing data-----------------" + e.getMessage());
            Toast.makeText(InfoActivity.this, "Failed to load data", Toast.LENGTH_LONG).show();
        }
        //Log.d("-----> RESULT", result);
        return entity;
    }


//    private Entity parseJsonByJsonOrg(String data) throws JSONException {
//        JSONObject jsonRoot = new JSONObject(data);
//        JSONArray numberArray = jsonRoot.getJSONArray("number");
//        String[] number = new String[numberArray.length()];
//        for (int i = 0; i < numberArray.length(); i++) {
//            number[i] = numberArray.getString(i);
//        }
//        String scheme = jsonRoot.getString("scheme");
//        String type = jsonRoot.getString("type");
//        String brand = jsonRoot.getString("brand");
//        boolean prepaid = Boolean.parseBoolean(jsonRoot.getString("prepaid"));
//        JSONArray countryArray = jsonRoot.getJSONArray("country");
//        String[] country = new String[countryArray.length()];
//        for (int i = 0; i < countryArray.length(); i++) {
//            country[i] = countryArray.getString(i);
//        }
//        JSONArray bankArray = jsonRoot.getJSONArray("bank");
//        String[] bank = new String[bankArray.length()];
//        for (int i = 0; i < bankArray.length(); i++) {
//            bank[i] = bankArray.getString(i);
//        }
//
//        Entity entity = new Entity();
//        entity.setScheme(scheme);
//        entity.setType(type);
//        entity.setBrand(brand);
//        entity.setPrepaid(prepaid);
//        Country countryEntity = new Country(
//                country[0],
//                country[1],
//                country[2],
//                country[3],
//                country[4],
//                Integer.parseInt(country[5]),
//                Integer.parseInt(country[5]));
//        entity.setCountry(countryEntity);
//        Bank bankEntity = new Bank(bank[0], bank[1], bank[2], bank[3]);
//        entity.setBank(bankEntity);
//        Number numberEntity = new Number(Integer.parseInt(number[0]), Boolean.parseBoolean(number[1]));
//        entity.setNumber(numberEntity);
//        Log.d("--> PARSED ENTITY", entity.toString());
//        return entity;
//    }

}