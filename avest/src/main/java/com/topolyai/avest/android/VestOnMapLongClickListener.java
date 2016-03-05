package com.topolyai.avest.android;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.topolyai.avest.Bootstrap;

public abstract class VestOnMapLongClickListener implements OnMapLongClickListener {

    private Bootstrap bootstrap;
    private Context context;

    public VestOnMapLongClickListener(Context c) {
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
