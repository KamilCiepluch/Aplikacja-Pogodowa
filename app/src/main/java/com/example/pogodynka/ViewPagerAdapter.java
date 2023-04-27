package com.example.pogodynka;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;


public class ViewPagerAdapter extends FragmentStateAdapter {



    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private final ArrayList<String> fragmentsTitle = new ArrayList<>();

    public ViewPagerAdapter(FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }


    public void addFragment(Fragment fragment, String fragmentTittle)
    {
        fragments.add(fragment);
        fragmentsTitle.add(fragmentTittle) ;
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }
}

