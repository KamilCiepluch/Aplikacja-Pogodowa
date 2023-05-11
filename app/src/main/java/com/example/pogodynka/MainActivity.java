package com.example.pogodynka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    final Long secondsToUpdate = 300L;
    private UpdateThread dataUpdateThread;
    private UpdateTimeThread currentTimeThread;

    ViewPagerAdapter adapter;
    TextView internetConnection;
    Boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isTablet = getResources().getBoolean(R.bool.is_tablet);

        internetConnection = findViewById(R.id.InternetConnection);
        if(isNetworkAvailable())
            internetConnection.setText("");
        else
            internetConnection.setText(R.string.notConnected);




        if (!isTablet){
            ViewPager2 viewPager = findViewById(R.id.viewPager);
            adapter = new ViewPagerAdapter(this);
            adapter.addFragment(new Fragment1());
            adapter.addFragment(new Fragment2());
            adapter.addFragment(new Fragment3());
            viewPager.setAdapter(adapter);
            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    Fragment fragment = adapter.getFragments().get(position);
                    Log.wtf("test", "fragment: " + fragment);
                    if(fragment!= null)
                    {
                        if(fragment instanceof  Fragment1)
                            ((Fragment1) fragment).update();

                        else if(fragment instanceof Fragment2)
                            ((Fragment2) fragment).update();

                        else if(fragment instanceof Fragment3)
                            ((Fragment3) fragment).update();
                    }
                    super.onPageSelected(position);
                }
            });
        }





        Button listActivity = findViewById(R.id.buttonList);
        listActivity.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ListActivity.class)));

        //todo poprawic
        Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(view -> {
            refreshData();

            Fragment1 fr1;
            Fragment2 fr2;
            Fragment3 fr3;


            if(isTablet)
            {
                fr1 = (Fragment1)getSupportFragmentManager().findFragmentById(R.id.fragment1);
                fr2 = (Fragment2)getSupportFragmentManager().findFragmentById(R.id.fragment2);
                fr3 = (Fragment3)getSupportFragmentManager().findFragmentById(R.id.fragment3);
            }
            else
            {
                ArrayList<Fragment> fragments = adapter.getFragments();
                fr1=   (Fragment1) fragments.get(0);
                fr2=   (Fragment2) fragments.get(1);
                fr3=   (Fragment3) fragments.get(2);
            }

            if(fr1!=null)
                fr1.update();
            if(fr2!=null)
                fr2.update();
            if(fr3!=null)
                fr3.update();
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dataUpdateThread!=null && dataUpdateThread.isAlive())
        {
           dataUpdateThread.stopThread();
           try {
               dataUpdateThread.join();
           }catch (Exception ignored){}

            Log.wtf("Thread test", "I destroyed listening thread");
        }

        if(currentTimeThread!=null && currentTimeThread.isAlive())
        {
            currentTimeThread.stopThread();
            try {
                currentTimeThread.join();
            }catch (Exception ignored){}

            Log.wtf("Thread test", "I destroyed listening thread");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Fragment> fragments ;
        if(isNetworkAvailable())
            internetConnection.setText("");
        else
            internetConnection.setText(R.string.notConnected);

        if(isTablet)
        {
            fragments = new ArrayList<>();
            Fragment1 fr1 = (Fragment1)getSupportFragmentManager().findFragmentById(R.id.fragment1);
            Fragment2 fr2 = (Fragment2)getSupportFragmentManager().findFragmentById(R.id.fragment2);
            Fragment3 fr3 = (Fragment3)getSupportFragmentManager().findFragmentById(R.id.fragment3);

            fragments.add(fr1);
            fragments.add(fr2);
            fragments.add(fr3);

        }
        else {
            fragments = adapter.getFragments();
        }
        if(dataUpdateThread== null)
        {
            dataUpdateThread = new UpdateThread(this,fragments, secondsToUpdate);
            dataUpdateThread.start();
            Log.wtf("Thread test", "I created new listening thread");
        }

        if(currentTimeThread== null)
        {

            currentTimeThread = new UpdateTimeThread(this,fragments);
            currentTimeThread.start();
            Log.wtf("Thread test", "I created new listening thread");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(dataUpdateThread!=null && dataUpdateThread.isAlive())
        {
            dataUpdateThread.stopThread();
            try {
                dataUpdateThread.join();
            }catch (Exception ignored){}
            dataUpdateThread = null;
            Log.wtf("Thread test", "I destroyed listening thread");
        }

        if(currentTimeThread!=null && currentTimeThread.isAlive())
        {
            currentTimeThread.stopThread();
            try {
                currentTimeThread.join();
            }catch (Exception ignored){}
            currentTimeThread = null;
            Log.wtf("Thread test", "I destroyed listening thread");
        }



    }

    void refreshData()
    {
        if(isNetworkAvailable())
            internetConnection.setText("");
        else
            internetConnection.setText(R.string.notConnected);

        String city = getSelectedCity();
        if(city==null) return;
        SharedPreferences cityToUpdate = getSharedPreferences(city, Context.MODE_PRIVATE);
        JsonThread.getData(city,cityToUpdate);
    }


    String getSelectedCity()
    {
        SharedPreferences data = getSharedPreferences("List", Context.MODE_PRIVATE);
        if(data==null) return null;
        return data.getString("SelectedCity",null);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}