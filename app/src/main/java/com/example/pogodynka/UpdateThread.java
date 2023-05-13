package com.example.pogodynka;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class UpdateThread extends Thread{

    enum ThreadFlag {Work, Stop}

    ThreadFlag flag;

    Long timeToUpdate;
    ArrayList<Fragment> fragments;
    Activity ac;
    public UpdateThread(@NonNull Activity activity, @NonNull ArrayList<Fragment> fragments, Long autoUpdateTimeSec)
    {
         this.ac = activity;
         this.fragments = fragments;
         timeToUpdate = autoUpdateTimeSec *1000;
         flag = ThreadFlag.Work;
    }


    @Override
    public void run() {
        Log.wtf("test", "Thread started");
        while (flag==ThreadFlag.Work)
        {
            refreshData();
            Log.wtf("Thread test", "Thread update!! :)");
            try {

                sleep(timeToUpdate);

            }catch (Exception e){
                Log.i("Thread wake", "Wake up UpdateThread");
            }
        }
        Log.wtf("test", "Thread finished");
    }


    public void stopThread()
    {
        Log.wtf("test", "Thread Stop method start");
        flag = ThreadFlag.Stop;
        this.interrupt();
        Log.wtf("test", "Thread Stop method finished");
    }
    void refreshData()
    {
        String city = getSelectedCity();
        if(city==null) return;
        Log.i("Thread selected city", city);
        SharedPreferences cityToUpdate = ac.getSharedPreferences(city, Context.MODE_PRIVATE);
        JsonThread.getData(city,cityToUpdate);
        if(fragments!=null)
        {
            Fragment1 fr1=   (Fragment1) fragments.get(0);
            Fragment2 fr2=   (Fragment2) fragments.get(1);
            Fragment3 fr3=   (Fragment3) fragments.get(2);
            fr1.update();
            fr2.update();
            fr3.update();
        }
    }


    String getSelectedCity()
    {
        SharedPreferences data = ac.getSharedPreferences("List", Context.MODE_PRIVATE);
        if(data==null) return null;
        return data.getString("SelectedCity",null);
    }
}
