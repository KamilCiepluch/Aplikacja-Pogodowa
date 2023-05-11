package com.example.pogodynka;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Fragment1 extends Fragment {

    private TextView cityName;
    private TextView coords;
    private TextView currentDate;
    private ImageView weatherImg;
    private TextView temp;
    private TextView tempMin;
    private TextView tempMax;
    private TextView weatherDesc;
    private TextView pressure;
    private TextView lastUpdate;




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
        update();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        update();
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




    public void update()
    {
        FragmentActivity activity = getActivity();
        if(activity==null)  return;
        SharedPreferences info = activity.getSharedPreferences("List", Context.MODE_PRIVATE);

        if(info == null) return;
        String city = info.getString("SelectedCity",null);
        String tempUnit = info.getString("TempUnit", null);
        if(city!=null)
        {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(city, Context.MODE_PRIVATE);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());


            Date date = new Date();
            String currentDateStr = getResources().getString(R.string.currentDate) + formatter.format(date);
            String icon = sharedPreferences.getString("icon",null);
            String tempStr;
            String tempMinStr;
            String tempMaxStr;
            if(tempUnit.equals("Kel"))
            {
                tempStr = (sharedPreferences.getInt("temp",0)+273) + "K";
                tempMinStr=getResources().getString(R.string.temp_min)
                        + (sharedPreferences.getInt( "tempMin",0)+273) + "K";
                tempMaxStr=getResources().getString(R.string.temp_max)
                        + (sharedPreferences.getInt( "tempMax",0)+273) + "K";
            }
            else {

                tempStr = sharedPreferences.getInt("temp",0) + "°C";
                tempMinStr =getResources().getString(R.string.temp_min)
                        + sharedPreferences.getInt( "tempMin",0) + "°C";
                tempMaxStr=getResources().getString(R.string.temp_max)
                        + sharedPreferences.getInt( "tempMax",0) + "°C";
            }

            String pressureStr = getResources().getString(R.string.pressure)
                    + sharedPreferences.getInt("pressure",0) + "hPa";
            String lastUpdateStr = getResources().getString(R.string.update_time) + sharedPreferences.getString("UpdateTime","");


            cityName.setText(city);
            coords.setText(sharedPreferences.getString("cityCoordinates",""));
            currentDate.setText(currentDateStr);
            temp.setText(tempStr );
            tempMin.setText(tempMinStr);
            tempMax.setText(tempMaxStr);
            weatherDesc.setText(sharedPreferences.getString("weather",""));
            pressure.setText(pressureStr);
            lastUpdate.setText(lastUpdateStr);
            if(icon!=null) {
                weatherImg.setImageResource(getImageID(icon));
            }
        }
        else
        {
            cityName.setText(getString(R.string.noData));
            coords.setText("");
            currentDate.setText("");
            temp.setText("" );
            tempMin.setText("");
            tempMax.setText("");
            weatherDesc.setText("");
            pressure.setText("");
            lastUpdate.setText("");
            weatherImg.setImageResource(0);
        }


    }

    public void updateDate()
    {
        FragmentActivity activity = getActivity();
        if(activity==null)  return;
        SharedPreferences info = activity.getSharedPreferences("List", Context.MODE_PRIVATE);
        if(info == null) return;
        String city = info.getString("SelectedCity",null);
        if(city!=null)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date date = new Date();
            String currentDateStr = getResources().getString(R.string.currentDate) + formatter.format(date);
            currentDate.setText(currentDateStr);
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


