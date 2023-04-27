package com.example.pogodynka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {




    private static TextView cityName;
    private static TextView coords;
    private static TextView currentDate;
    private static ImageView weatherImg;
    private static TextView temp;
    private static TextView tempMin;
    private static TextView tempMax;
    private static TextView weatherDesc;
    private static TextView pressure;
    private static TextView windDir;
    private static TextView windSpeed;
    private static TextView humidity;
    private static TextView visibility;
    private static TextView lastUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



       // init();
        SharedPreferences dataList = getSharedPreferences("List", Context.MODE_PRIVATE);
        String city = dataList.getString("SelectedCity","");
        /*
        if(city!=null)
        {
          //  loadData(city);
        }
        else {

        }
*/
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);




        adapter.addFragment(new Fragment1(), "F1");
        adapter.addFragment(new fragment2(), "F2");
        adapter.addFragment(new fragment3(), "F3");
        viewPager.setAdapter(adapter);




        Button listActivity = findViewById(R.id.buttonList);
        listActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ListActivity.class));
            }
        });


        Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               ArrayList<Fragment> fragments = adapter.getFragments();
                Fragment1 fr1=   (Fragment1) fragments.get(0);
                fr1.update(city);


            }
        });


    }


    private void loadData(String city)
    {
        cityName.setText(city);
        SharedPreferences sharedPreferences = getSharedPreferences(city, Context.MODE_PRIVATE);
        coords.setText(sharedPreferences.getString("cityCoordinates",""));

        //todo poprawic dopsiac obecna date
        currentDate.setText(
                getResources().getString(R.string.currentDate)

        );
        //todo to samo z weatherimg
        // weatherImg


        temp.setText(sharedPreferences.getString("temp",""));
        tempMin.setText(getResources().getString(R.string.temp_min) + sharedPreferences.getString( "tempMin",""));
        tempMax.setText(getResources().getString(R.string.temp_max) + sharedPreferences.getString( "tempMax",""));
        weatherDesc.setText(sharedPreferences.getString("weather",""));
        pressure.setText(getResources().getString(R.string.pressure) + sharedPreferences.getString("pressure",""));
        windDir.setText(getResources().getString(R.string.windDir) + sharedPreferences.getString("windDir",""));
        windSpeed.setText(getResources().getString(R.string.windSpeed) + sharedPreferences.getString("windSpeed",""));
        humidity.setText(getResources().getString(R.string.Humidity) + sharedPreferences.getString("humidity",""));
        visibility.setText(getResources().getString(R.string.Visibility) + sharedPreferences.getString("visibility",""));;
        lastUpdate.setText(getResources().getString(R.string.update_time) + sharedPreferences.getString("UpdateTime",""));;;
    }



    private void init()
    {

        cityName = findViewById(R.id.city);
        coords = findViewById(R.id.city_coord);
        currentDate = findViewById(R.id.currentDate);
        weatherImg = findViewById(R.id.image);
        temp = findViewById(R.id.temp);
        tempMin = findViewById(R.id.TempMin);
        tempMax= findViewById(R.id.TempMax);
        weatherDesc = findViewById(R.id.Weather_Status);
        pressure = findViewById(R.id.pressure);
        windDir = findViewById(R.id.windDir);
        windSpeed = findViewById(R.id.windSpeed);
        humidity = findViewById(R.id.Humidity);
        visibility = findViewById(R.id.Visibility);
        lastUpdate = findViewById(R.id.updateTime);

    }


}