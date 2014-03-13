package com.ayysir.paek.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ayysir.paek.fragments.webview.ChangelogFragment;
import com.ayysir.paek.fragments.webview.GitFragment;
import com.ayysir.paek.fragments.webview.GplusFragment;


public class OverViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    Context context;
    // Tab Titles
    private String tabtitles[] = new String[]{"CHANGELOG", "GITHUB", "GPLUS"};

    public OverViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                ChangelogFragment chg = new ChangelogFragment();
                return chg;
            case 1:
                GitFragment git = new GitFragment();
                return git;
            case 2:
                GplusFragment gp = new GplusFragment();
                return gp;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }


}
