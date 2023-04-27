package com.example.pogodynka;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
}
