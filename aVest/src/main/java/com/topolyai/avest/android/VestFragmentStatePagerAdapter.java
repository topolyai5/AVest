package com.topolyai.avest.android;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.topolyai.avest.Bootstrap;

public abstract class VestFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private Bootstrap bootstrap;

    public VestFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, false);
    }

    protected Bootstrap getBootstrap() {
        return bootstrap;
    }

}
