package com.example.pogodynka;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class Fragment2 extends Fragment {



    private TextView pressure;
    private TextView dir;
    private TextView speed;
    private TextView humidity;
    private TextView visibility;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fragment2, container, false);
        initFields(view);
        update("Krakow");
        return view;
    }





    private void initFields(View view)
    {
        pressure = view.findViewById(R.id.pressure);
        dir = view.findViewById(R.id.windDir);
        speed = view.findViewById(R.id.windSpeed);
        humidity = view.findViewById(R.id.Humidity);
        visibility = view.findViewById(R.id.Visibility);
    }

    public void update(  String city)
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(city, Context.MODE_PRIVATE);
        String pStr = getResources().getString(R.string.pressure) + " " + sharedPreferences.getInt("pressure",0);
        String wDirStr = getResources().getString(R.string.windDir) +" " + sharedPreferences.getString("windDir","");
        String wSpeedStr = getResources().getString(R.string.windSpeed) +" " + sharedPreferences.getFloat("windSpeed",0);
        String humStr = getResources().getString(R.string.Humidity) +" " + sharedPreferences.getInt("humidity",0);
        String visStr = getResources().getString(R.string.Visibility) +" " + sharedPreferences.getInt("visibility",0);
        pressure.setText(pStr);
        dir.setText(wDirStr);
        speed.setText(wSpeedStr);
        humidity.setText(humStr);
        visibility.setText(visStr);
    }



}