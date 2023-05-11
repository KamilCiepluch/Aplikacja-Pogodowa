package com.example.pogodynka;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class UpdateTimeThread extends  Thread{


    enum ThreadFlag {Work, Stop}

    UpdateThread.ThreadFlag flag;

    Long timeToUpdate;
    ArrayList<Fragment> fragments;

    Activity ac;
    public UpdateTimeThread(@NonNull Activity activity, @NonNull ArrayList<Fragment> fragments)
    {
        this.ac = activity;
        this.fragments = fragments;
        flag = UpdateThread.ThreadFlag.Work;
    }


    @Override
    public void run() {
        Log.wtf("test", "Thread started");
        while (flag== UpdateThread.ThreadFlag.Work)
        {
            refreshData();
            Log.wtf("Thread test", "Thread time update!! :)");
            try {

                sleep(60 *1000);

            }catch (Exception e){
                Log.i("Thread wake", "Wake up sleepy Joe");
            }
        }
        Log.wtf("test", "Thread finished");
    }


    public void stopThread()
    {
        Log.wtf("test", "Thread Stop method start");
        flag = UpdateThread.ThreadFlag.Stop;
        this.interrupt();
        Log.wtf("test", "Thread Stop method finished");
    }
    void refreshData()
    {
        String city = getSelectedCity();
        if(city==null) return;
        Log.i("Thread selected city", city);
        if(fragments!=null)
        {
            Fragment1 fr1=   (Fragment1) fragments.get(0);
          //  Fragment2 fr2=   (Fragment2) fragments.get(1);
           // Fragment3 fr3=   (Fragment3) fragments.get(2);
            fr1.updateDate();
           // fr2.update();
          //  fr3.update();
        }
    }


    String getSelectedCity()
    {
        SharedPreferences data = ac.getSharedPreferences("List", Context.MODE_PRIVATE);
        if(data==null) return null;
        return data.getString("SelectedCity",null);
    }


}
