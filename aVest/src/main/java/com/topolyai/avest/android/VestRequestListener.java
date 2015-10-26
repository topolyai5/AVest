package com.topolyai.avest.android;

import android.content.Context;

import com.topolyai.avest.Bootstrap;

public abstract class VestRequestListener<T> { //implements RequestListener<T> {

    private Bootstrap bootstrap;
    private Context context;

    public VestRequestListener(Context c) {
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
