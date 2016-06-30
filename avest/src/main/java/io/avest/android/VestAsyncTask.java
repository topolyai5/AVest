package io.avest.android;

import android.os.AsyncTask;

import io.avest.Bootstrap;

/**
 * Created by geri on 7/5/2015.
 */
public abstract class VestAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private Bootstrap bootstrap;

    public VestAsyncTask() {
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, false);
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }
}
