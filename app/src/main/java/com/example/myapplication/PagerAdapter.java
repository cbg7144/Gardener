package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private OneFragment fragment1;
    private TwoFragment fragment2;
    private ThreeFragment fragment3;

    public PagerAdapter(AppCompatActivity activity) {

        super(activity);

    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                fragment1 = new OneFragment();
                addFragment(fragment1);
                break;
            case 1:
                fragment2 = new TwoFragment();
                addFragment(fragment2);
                break;
            case 2:
                fragment3 = new ThreeFragment();
                addFragment(fragment3);
                break;
            default:
                return null;
        }

        return mFragmentList.get(position);

    }

    public void addFragment(Fragment fragment){

        mFragmentList.add(fragment);

    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }
}
