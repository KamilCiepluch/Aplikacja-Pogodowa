package com.example.pogodynka;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class JsonThread extends Thread {

    private JSONObject jsonObject;
    private final String stringUrl;
    public JsonThread(String url)
    {
        stringUrl = url;
    }

    @Override
    public void run()
    {
        HttpURLConnection urlConnection = null;
        try {

            URL url = new URL( stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            // Ustawienie parametrów połączenia
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            // Uzyskanie strumienia wejściowego z połączenia
            InputStream inputStream = urlConnection.getInputStream();

            // Odczytanie danych z strumienia wejściowego przy użyciu InputStreamReader
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // Konwersja danych do formatu JSON
            String json = stringBuilder.toString();
            jsonObject = new JSONObject(json);

            // Tutaj można dokonać operacji na danych z pliku JSON

        } catch (Exception e) {
            // Obsługa wyjątków
            e.printStackTrace();
        } finally {
            // Zamykanie połączenia
            if(urlConnection!=null)
                urlConnection.disconnect();
        }
    }



    public JSONObject getJsonObject() {
        return jsonObject;
    }





    public static void getData(String city,SharedPreferences sharedPreferences)
    {

        String stringUrl =
                "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=f593804bcaa9ba0a3077e36ebf63bd6a&units=metric";

        JsonThread thread = new JsonThread(stringUrl);
        thread.start();
        try {
            thread.join();
        }
        catch (Exception ignored) {}

        JSONObject json =  thread.getJsonObject();
        thread.JsonParser(json,sharedPreferences);

        try {
            Double lon = (Double) json.getJSONObject("coord").get("lon");
            Double lat = (Double) json.getJSONObject("coord").get("lat");
            stringUrl = "https://api.openweathermap.org/data/2.5/forecast?lat="+lat+
                    "&lon="+lon+"&appid=f593804bcaa9ba0a3077e36ebf63bd6a&units=metric";
            JsonThread thread1 = new JsonThread(stringUrl);
            thread1.start();
            thread1.join();
            JSONObject json2 =  thread1.getJsonObject();
            thread1.JsonParserWeek(json2,sharedPreferences);

        }
        catch (Exception e)
        {
            Log.wtf("error", e.getMessage());
        }



    }

    public void JsonParser(JSONObject json, SharedPreferences sharedPreferences)
    {

        try {
            Double lon = (Double) json.getJSONObject("coord").get("lon");
            Double lat = (Double) json.getJSONObject("coord").get("lat");
            Integer pressure = (Integer) json.getJSONObject("main").get("pressure");
            Integer visibility =((Integer) json.get("visibility"))/1000;
            Integer humidity = (Integer) json.getJSONObject("main").get("humidity");
            String weather =  (String) json.getJSONArray("weather").getJSONObject(0).get("main");
            String weatherDesc =  (String) json.getJSONArray("weather").getJSONObject(0).get("description");
            Double temp =     (Double) json.getJSONObject("main").get("temp");
            Double tempMax =  (Double) json.getJSONObject("main").get("temp_max");
            Double tempMin =  (Double) json.getJSONObject("main").get("temp_min");
            String icon = (String) json.getJSONArray("weather").getJSONObject(0).get("icon");



            //todo coś dziwnego tu sie dzieje
            Integer windDirTmp = (Integer) json.getJSONObject("wind").get("deg");
            Double windSpeed = (Double) json.getJSONObject("wind").get("speed");
            String windDir = windDirParser(Double.valueOf(windDirTmp)).toString();


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = new Date();
            String currentDateStr = formatter.format(date);


            SharedPreferences.Editor editor = sharedPreferences.edit();



            editor.putString("UpdateTime", currentDateStr);
            editor.putString("temp", Integer.toString(Math.round(temp.floatValue())));
            editor.putString("tempMax", Integer.toString(Math.round(tempMax.floatValue())));
            editor.putString("tempMin", Integer.toString(Math.round(tempMin.floatValue())));
            editor.putString("cityCoordinates",lat + " "+ lon);
            editor.putInt("pressure",pressure);
            editor.putString("weather",weather);
            editor.putString("weatherDesc",weatherDesc);
            editor.putString("windDir",windDir);
            editor.putFloat("windSpeed", new Float(windSpeed));
            editor.putInt("humidity",humidity);
            editor.putInt("visibility",visibility);
            editor.putString("icon",icon);
            editor.apply();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void JsonParserWeek(JSONObject json, SharedPreferences sharedPreferences)
    {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for(int i=0; i<5; i++)
            {
                Double t  =  (Double) json.getJSONArray("list").getJSONObject(i*8+3).getJSONObject("main").get("temp");
                String temp = Integer.toString(Math.round(t.floatValue()));
                String weather =(String) json.getJSONArray("list").getJSONObject(i*8+3).getJSONArray("weather").getJSONObject(0).get("main");
                String desc = (String) json.getJSONArray("list").getJSONObject(i*8+3).getJSONArray("weather").getJSONObject(0).get("description");
                String icon = (String) json.getJSONArray("list").getJSONObject(i*8+3).getJSONArray("weather").getJSONObject(0).get("icon");
                String d = (String) json.getJSONArray("list").getJSONObject(i*8+3).get("dt_txt");

                int year = Integer.parseInt(d.substring(0,4));
                int month = Integer.parseInt(d.substring(5,7));
                int day = Integer.parseInt(d.substring(8,10));
                String date = getDayString( new GregorianCalendar(year,month-1, day).getTime(), Locale.US);
                editor.putString("temp" + i, temp);
                editor.putString("weather"+i,weather);
                editor.putString("desc"+i,desc);
                editor.putString("icon"+i,icon);
                editor.putString("weather",weather);
                editor.putString("date"+i,date);
            }

            editor.apply();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
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


    public static String getDayString(Date date, Locale locale) {
        DateFormat formatter = new SimpleDateFormat("MMM EE d", locale);
        return formatter.format(date);
    }


}
