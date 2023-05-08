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
        pressure = view.findViewById(R.id.pressure);
        dir = view.findViewById(R.id.windDir);
        speed = view.findViewById(R.id.windSpeed);
        humidity = view.findViewById(R.id.Humidity);
        visibility = view.findViewById(R.id.Visibility);
    }

    public void update()
    {

        FragmentActivity activity = getActivity();
        if(activity==null)  return;
        SharedPreferences data = activity.getSharedPreferences("List", Context.MODE_PRIVATE);
        if(data==null) return;
        String city = data.getString("SelectedCity",null);

        if(city!=null)
        {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(city, Context.MODE_PRIVATE);
            String pStr = getResources().getString(R.string.pressure) + " " + sharedPreferences.getInt("pressure",0) +"hPa";
            String wDirStr = getResources().getString(R.string.windDir) +" " + sharedPreferences.getString("windDir","");
            String wSpeedStr = getResources().getString(R.string.windSpeed) +" " + sharedPreferences.getFloat("windSpeed",0) + " km/h";
            String humStr = getResources().getString(R.string.Humidity) +" " + sharedPreferences.getInt("humidity",0) + "%";
            String visStr = getResources().getString(R.string.Visibility) +" " + sharedPreferences.getInt("visibility",0)+"km";
            pressure.setText(pStr);
            dir.setText(wDirStr);
            speed.setText(wSpeedStr);
            humidity.setText(humStr);
            visibility.setText(visStr);
        }
        else
        {
            pressure.setText(getString(R.string.noData));
            dir.setText("");
            speed.setText("");
            humidity.setText("");
            visibility.setText("");
        }


    }






}