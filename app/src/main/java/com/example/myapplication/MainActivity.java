package com.example.myapplication;

import static androidx.core.splashscreen.SplashScreen.installSplashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.viewpager2.widget.ViewPager2;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ViewPager2 viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = installSplashScreen(this);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 초수 2000 설정, 변동 가능
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            splashScreen.setKeepOnScreenCondition(() -> false);
        }, 800);


        setViewPager();
        setTabLayout();
    }

    private void setViewPager(){
        viewPager = binding.pager;
        pagerAdapter = new PagerAdapter(this);

        pagerAdapter.createFragment(0);
        pagerAdapter.createFragment(1);
        pagerAdapter.createFragment(2);
        viewPager.setAdapter(pagerAdapter);
    }

    private void setTabLayout(){
        tabLayout = binding.tabLayout;
        String[] tabTitles = {"Contact", "Gallery", "Todo"};
        int[] tabIcons = {R.drawable.ic_contact, R.drawable.ic_gallery, R.drawable.ic_check};

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setText(tabTitles[position]); // 텍스트 설정
                    tab.setIcon(tabIcons[position]); // 이미지 설정
                }
        ).attach();
    }
}