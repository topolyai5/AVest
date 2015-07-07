package com.topolyai.avest.android;


import com.topolyai.avest.Bootstrap;

import android.content.Context;
import android.view.MenuItem.OnMenuItemClickListener;

public abstract class VestOnMenuItemClickListener implements OnMenuItemClickListener {

    private Bootstrap bootstrap;
    private Context context;

    public VestOnMenuItemClickListener(Context c) {
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, false);
        context = c;
    }

    public Context getContext() {
        return context;
    }

    protected Bootstrap getBootstrap() {
        return bootstrap;
    }
}
