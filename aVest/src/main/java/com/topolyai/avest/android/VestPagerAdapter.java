package com.topolyai.avest.android;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import com.topolyai.avest.Bootstrap;

public abstract class VestPagerAdapter extends PagerAdapter {

    private Bootstrap bootstrap;

    public VestPagerAdapter() {
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, false);
    }

    protected Bootstrap getBootstrap() {
        return bootstrap;
    }

}
