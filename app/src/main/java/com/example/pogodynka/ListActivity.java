package com.example.pogodynka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ListActivity extends AppCompatActivity {


    private List<String> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);




        ListView list = findViewById(R.id.citieslist);
        SharedPreferences sharedPreferences = getSharedPreferences("List", Context.MODE_PRIVATE);


        Set<String> set = sharedPreferences.getStringSet("cities", null);
        if (set != null) {
            items.addAll(set);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ListActivity.this,
                    android.R.layout.simple_list_item_1, items);

            list.setAdapter(adapter);

        }

        TextView input = findViewById(R.id.cityInput);
        Button add = findViewById(R.id.buttonAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo poprawic sprawdzajac input


                String city = input.getText().toString();
                if(!items.contains(city))
                {
                    String stringUrl =
                            "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=f593804bcaa9ba0a3077e36ebf63bd6a";

                    JsonThread thread = new JsonThread(stringUrl);
                    thread.start();
                    try {
                        thread.join();
                    }
                    catch (Exception ignored) {}

                    JSONObject json =  thread.getJsonObject();
                    JsonParser(json,city);
                    items.add(city);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ListActivity.this,
                            android.R.layout.simple_list_item_1, items);

                    list.setAdapter(adapter);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Set<String> set = new HashSet<>(items);
                    editor.putStringSet("cities", set);
                    editor.putString("SelectedCity", city);
                    editor.apply();
                }
                else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("SelectedCity", city);
                    editor.apply();
                }
            }
        });


        Button remove = findViewById(R.id.buttonRemove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo poprawic sprawdzajac input
                String city = input.getText().toString();
                if(items.contains(city))
                {
                    items.remove(city);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ListActivity.this,
                            android.R.layout.simple_list_item_1, items);
                    list.setAdapter(adapter);


                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Set<String> set = new HashSet<>(items);
                    editor.putStringSet("cities",set);
                    editor.apply();


                    SharedPreferences cityData = getSharedPreferences(city, Context.MODE_PRIVATE);
                    if(cityData!=null)
                    {
                        SharedPreferences.Editor editorTmp = cityData.edit();
                        editorTmp.clear();
                        editorTmp.apply();
                    }

                    if(sharedPreferences.getString("SelectedCity","").equals(city))
                    {
                        editor.remove("SelectedCity");
                    }

                }
            }
        });


        Button select = findViewById(R.id.buttonSelect);
        select.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                TextView selectedCity = findViewById(R.id.selectedCity);
                if(items.contains(input.getText().toString()))
                {
                    selectedCity.setText("Selected city: " + input.getText().toString());
                }
               else
                   selectedCity.setText("There's not such city on the list");
            }
        });

    }


    public void JsonParser(JSONObject json, String city)
    {

        try {
            Double lon = (Double) json.getJSONObject("coord").get("lon");
            Double lat = (Double) json.getJSONObject("coord").get("lat");
            Integer pressure = (Integer) json.getJSONObject("main").get("pressure");
            Integer visibility =(Integer) json.get("visibility");
            Integer humidity = (Integer) json.getJSONObject("main").get("humidity");
            String weather =  (String) json.getJSONArray("weather").getJSONObject(0).get("main");
            String weatherDesc =  (String) json.getJSONArray("weather").getJSONObject(0).get("description");
            Double t1 =     (Double) json.getJSONObject("main").get("temp");
            Double tMax1 =  (Double) json.getJSONObject("main").get("temp_max");
            Double tMin1 =  (Double) json.getJSONObject("main").get("temp_min");
            String temp= temperatureParser(t1);
            String tempMax =  temperatureParser(tMax1);
            String tempMin =  temperatureParser(tMin1);

           //todo coś dziwnego tu sie dzieje
            Integer windDirTmp = (Integer) json.getJSONObject("wind").get("deg");
            Double windSpeed = (Double) json.getJSONObject("wind").get("speed");
            String windDir = windDirParser(Double.valueOf(windDirTmp)).toString();
            Integer uT = (Integer) json.get("dt");
            Timestamp timestamp = new Timestamp(uT);
            String updateTime= timestamp.toString();



            SharedPreferences sharedPreferences = getSharedPreferences(city, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UpdateTime", updateTime);
            editor.putString("temp", temp);
            editor.putString("tempMax", tempMax);
            editor.putString("tempMin", tempMin);
            editor.putString("cityCoordinates",lat + " "+ lon);
            editor.putInt("pressure",pressure);
            editor.putString("weather",weather);
            editor.putString("weatherDesc",weatherDesc);
            editor.putString("windDir",windDir);
            editor.putFloat("windSpeed", new Float(windSpeed));
            editor.putInt("humidity",humidity);
            editor.putInt("visibility",visibility);
            editor.apply();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



   private String temperatureParser(Double tmp)
    {
        Integer res = (int)((tmp -32.0)/1.8);
        return res + "°C";
    }

    private Dir windDirParser(Double dir)
    {
        if(dir >= 348.75 && dir<= 360.0  || dir>0 && dir< 11.25 ) return Dir.N;
        else if(dir>=11.25 && dir< 33.75) return Dir.NNE;
        else if(dir>=33.75 && dir< 56.25) return Dir.NE;
        else if(dir>=56.25 && dir< 78.75) return Dir.ENE;
        else if(dir>=78.75 && dir< 101.25) return Dir.E;
        else if(dir>=101.25 && dir< 123.75) return Dir.ESE;
        else if(dir>=123.75 && dir< 146.25) return Dir.SE;
        else if(dir>=146.25 && dir< 168.75) return Dir.SSE;
        else if(dir>=168.75 && dir<191.25) return Dir.S;
        else if(dir>=191.25 && dir< 213.75) return Dir.SSW;
        else if(dir>=213.75 && dir< 236.25) return Dir.SW;
        else if(dir>=236.25 && dir< 258.75) return Dir.WSW;
        else if(dir>=258.75 && dir< 281.25) return Dir.W;
        else if(dir>=281.25 && dir<  303.75) return Dir.WNW;
        else if(dir>=303.75 && dir< 326.25) return Dir.NW;
        else return Dir.NNW;

    }

}
