package io.avest.android;

import android.support.v4.view.PagerAdapter;

import io.avest.Bootstrap;

public abstract class VestPagerAdapter extends PagerAdapter {

    private Bootstrap bootstrap;

    public VestPagerAdapter() {
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, false);
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

}
