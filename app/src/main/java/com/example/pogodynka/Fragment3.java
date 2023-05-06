package com.example.pogodynka;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SymbolTable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Fragment3 extends Fragment {

    private List<TextView> temps;
    private List<TextView> days;

    private List<ImageView> weatherIcons;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fragment3, container, false);
        initFields(view);
        update();
        return view;

    }





    private void initFields(View view)
    {
        days = new ArrayList<>();
        days.add(view.findViewById(R.id.day1));
        days.add(view.findViewById(R.id.day2));
        days.add(view.findViewById(R.id.day3));
        days.add(view.findViewById(R.id.day4));
        days.add(view.findViewById(R.id.day5));


        weatherIcons = new ArrayList<>();
        weatherIcons.add(view.findViewById(R.id.weather1));
        weatherIcons.add(view.findViewById(R.id.weather2));
        weatherIcons.add(view.findViewById(R.id.weather3));
        weatherIcons.add(view.findViewById(R.id.weather4));
        weatherIcons.add(view.findViewById(R.id.weather5));

        temps  = new ArrayList<>();
        temps.add(view.findViewById(R.id.temp1));
        temps.add(view.findViewById(R.id.temp2));
        temps.add(view.findViewById(R.id.temp3));
        temps.add(view.findViewById(R.id.temp4));
        temps.add(view.findViewById(R.id.temp5));

    }





    public void update()
    {

        FragmentActivity activity = getActivity();
        if(activity==null)  return;
        SharedPreferences data = activity.getSharedPreferences("List", Context.MODE_PRIVATE);
        if(data==null) return;
        String city = data.getString("SelectedCity",null);

        SharedPreferences sharedPreferences = activity.getSharedPreferences(city, Context.MODE_PRIVATE);
        if(sharedPreferences==null) return;
        int i=0;
        for(TextView tmp: temps)
        {
            String temp = sharedPreferences.getString("temp" +i,"") + "°C";
            tmp.setText(temp);
            i++;
        }
        i=0;
        for(TextView date: days)
        {
            String temp = sharedPreferences.getString("date" +i,"");
            date.setText(temp);
            i++;
        }

        i=0;
        for(ImageView imageView:weatherIcons)
        {
            String temp = sharedPreferences.getString("icon" +i,null);
            if(temp!=null)
            {
                imageView.setImageResource(getImageID(temp));
            }
            i++;
        }
    }



        public int getImageID(String name)
        {
            switch (name)
            {
                case "01d": return R.drawable._01d;
                case "01n": return R.drawable._01n;
                case "02d": return R.drawable._02d;
                case "02n": return R.drawable._02n;
                case "03d": return R.drawable._03d;
                case "03n": return R.drawable._03n;
                case "04d": return R.drawable._04d;
                case "04n": return R.drawable._04n;
                case "09d": return R.drawable._09d;
                case "09n": return R.drawable._09n;
                case "10d": return R.drawable._10d;
                case "10n": return R.drawable._10n;
                case "11d": return R.drawable._11d;
                case "11n": return R.drawable._11n;
                case "13d": return R.drawable._13d;
                case "13n": return R.drawable._13n;
                case "50d": return R.drawable._50d;
                case "50n": return R.drawable._50n;
            }
            return R.drawable._01d;
        }

}