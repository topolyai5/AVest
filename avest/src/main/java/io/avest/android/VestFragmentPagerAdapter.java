package io.avest.android;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.avest.Bootstrap;

public abstract class VestFragmentPagerAdapter extends FragmentPagerAdapter {

    private Bootstrap bootstrap;

    public VestFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, false);
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

}
