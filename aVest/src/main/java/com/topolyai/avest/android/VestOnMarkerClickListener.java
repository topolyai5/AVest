package com.topolyai.avest.android;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.topolyai.avest.Bootstrap;

public abstract class VestOnMarkerClickListener implements OnMarkerClickListener {
    private Bootstrap bootstrap;
    private Context context;

    public VestOnMarkerClickListener(Context c) {
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
