package io.avest.android;

import android.content.Context;

import io.avest.Bootstrap;

public abstract class VestListenerBase {

    private Bootstrap bootstrap;
    private Context context;

    public VestListenerBase(Context c) {
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, false);
        context = c;
    }

    public Context getContext() {
        return context;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

}
