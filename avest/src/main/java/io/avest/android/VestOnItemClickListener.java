package io.avest.android;

import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;

import io.avest.Bootstrap;

public abstract class VestOnItemClickListener implements OnItemClickListener {

    private Bootstrap bootstrap;
    private Context context;

    public VestOnItemClickListener(Context c) {
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
