package com.example.pogodynka;

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


public class Fragment1 extends Fragment {

    private static TextView cityName;
    private static TextView coords;
    private static TextView currentDate;
    private static ImageView weatherImg;
    private static TextView temp;
    private static TextView tempMin;
    private static TextView tempMax;
    private static TextView weatherDesc;
    private static TextView pressure;
    private static TextView lastUpdate;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fragment1, container, false);
        initFields(view);
        return view;

    }




    private void initFields(View view)
    {
         cityName = view.findViewById(R.id.city);
         coords = view.findViewById(R.id.city_coord);
         currentDate = view.findViewById(R.id.currentDate);
         weatherImg = view.findViewById(R.id.image);
         temp = view.findViewById(R.id.temp);
         tempMin = view.findViewById(R.id.TempMin);
         tempMax= view.findViewById(R.id.TempMax);
         weatherDesc = view.findViewById(R.id.Weather_Status);
         pressure = view.findViewById(R.id.pressure);
         lastUpdate = view.findViewById(R.id.updateTime);
    }




    public void update(String city)
    {
        cityName.setText(city);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(city, Context.MODE_PRIVATE);
        coords.setText(sharedPreferences.getString("cityCoordinates",""));

        //todo poprawic dopsiac obecna date
        currentDate.setText(
                getResources().getString(R.string.currentDate)

        );
        //todo to samo z weatherimg
        // weatherImg
        temp.setText(sharedPreferences.getString("temp","") );
        tempMin.setText(getResources().getString(R.string.temp_min) + sharedPreferences.getString( "tempMin",""));
        tempMax.setText(getResources().getString(R.string.temp_max) + sharedPreferences.getString( "tempMax",""));
        weatherDesc.setText(sharedPreferences.getString("weather",""));
        pressure.setText(getResources().getString(R.string.pressure) + sharedPreferences.getInt("pressure",0));
        lastUpdate.setText(getResources().getString(R.string.update_time) + sharedPreferences.getString("UpdateTime",""));

    }




}


