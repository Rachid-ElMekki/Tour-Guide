package com.example.lenovo.testmap.Controlers.Activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.lenovo.testmap.Adapters.PageAdapter;

import com.example.lenovo.testmap.R;

public class MapsActivity extends FragmentActivity {
    private ViewPager pager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.lenovo.testmap.R.layout.activity_maps);

        this.configureViewPagerAndTabs();


    }



    private void configureViewPagerAndTabs(){
               //Get ViewPager from layout
        pager = findViewById(R.id.activity_main_viewpager);
        //Set Adapter PageAdapter and glue it together
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(), this.getApplicationContext()));

        // 1 - Get TabLayout from layout
        android.support.design.widget.TabLayout tabs= findViewById(R.id.activity_main_tabs);
        // 2 - Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);
        // 3 - Design purpose. Tabs have the same width
        tabs.setTabMode(android.support.design.widget.TabLayout.MODE_FIXED);



    }



}
