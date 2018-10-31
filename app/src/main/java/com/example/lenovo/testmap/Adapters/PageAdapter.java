package com.example.lenovo.testmap.Adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lenovo.testmap.Controlers.Fragments.HelpFragment;
import com.example.lenovo.testmap.Controlers.Fragments.MapFragment;
import com.example.lenovo.testmap.Controlers.Fragments.PageFragment;
import com.example.lenovo.testmap.R;

import static java.security.AccessController.getContext;


public class PageAdapter extends FragmentPagerAdapter {

    private static PageFragment pageFragment;
    private  HelpFragment helpFragment;
    private static Context context;


    public PageAdapter(FragmentManager mgr, Context c) {
        super(mgr);
        pageFragment = PageFragment.newInstance();
        helpFragment = HelpFragment.newInstance();
        context=c;
    }


    @Override
    public int getCount() {
        return(3);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
        case 0: //Page number 1
            return pageFragment;
        case 1: //Page number 2
            return MapFragment.newInstance();
        case 2: //Page number 3
            return helpFragment;
        default:
            return null;
    }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0: //Page number 1
                return context.getResources().getString(R.string.ViewWTD);
            case 1: //Page number 2
                return context.getResources().getString(R.string.ViewMap);
            case 2: //Page number 3
                return context.getResources().getString(R.string.ViewParamHelp);
            default:
                return null;
        }
    }

    public  static PageFragment getPageFragment() {
        return pageFragment;
    }

    public void setPageFragment(PageFragment pageFragment) {
        this.pageFragment = pageFragment;
    }

    public  HelpFragment getHelpFragment() {
        return helpFragment;
    }

    public void setHelpFragment(HelpFragment helpFragment) {
        this.helpFragment = helpFragment;
    }
}

