package com.topolyai.avest.android;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.topolyai.avest.Bootstrap;

public abstract class VestFragmentPagerAdapter extends FragmentPagerAdapter {

    private Bootstrap bootstrap;

    public VestFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, false);
    }

    protected Bootstrap getBootstrap() {
        return bootstrap;
    }

}
