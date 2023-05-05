package com.example.pogodynka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
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
                        {
                            ((Fragment1) fragment).update();
                        }
                        else if(fragment instanceof Fragment2)
                        {
                           ((Fragment2) fragment).update();
                        }
                        else if(fragment instanceof Fragment3)
                        {
                            ((Fragment3) fragment).update();
                        }
                    }
                    super.onPageSelected(position);

                }
            }
        );



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
               /*
                Fragment1 fr1=   (Fragment1) fragments.get(0);
                Fragment2 fr2=   (Fragment2) fragments.get(1);
                Fragment3 fr3=   (Fragment3) fragments.get(2);
                fr1.update(city);


                SharedPreferences sharedPreferences = getSharedPreferences(city, Context.MODE_PRIVATE);
                fr2.update(null,sharedPreferences);
                */

            }
        });


    }
}