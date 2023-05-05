package com.example.pogodynka;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class Fragment3 extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment3, container, false);
    }
    public void update()
    {
        SharedPreferences data = getActivity().getSharedPreferences("List", Context.MODE_PRIVATE);
        String city = data.getString("SelectedCity",null);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(city, Context.MODE_PRIVATE);
        String pStr = getResources().getString(R.string.pressure) + " " + sharedPreferences.getInt("pressure",0);
        String wDirStr = getResources().getString(R.string.windDir) +" " + sharedPreferences.getString("windDir","");
        String wSpeedStr = getResources().getString(R.string.windSpeed) +" " + sharedPreferences.getFloat("windSpeed",0);
        String humStr = getResources().getString(R.string.Humidity) +" " + sharedPreferences.getInt("humidity",0);
        String visStr = getResources().getString(R.string.Visibility) +" " + sharedPreferences.getInt("visibility",0);

      /*
        pressure.setText(pStr);
        dir.setText(wDirStr);
        speed.setText(wSpeedStr);
        humidity.setText(humStr);
        visibility.setText(visStr);


       */
    }

}