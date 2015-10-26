package com.topolyai.avest.android;

import android.content.Context;
import android.view.View.OnClickListener;

import com.topolyai.avest.Bootstrap;

public abstract class VestOnClickListener implements OnClickListener {

    private Bootstrap bootstrap;
    private Context context;

    public VestOnClickListener(Context c) {
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
