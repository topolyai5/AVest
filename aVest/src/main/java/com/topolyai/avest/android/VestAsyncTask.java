package com.topolyai.avest.android;

import android.content.Context;
import android.os.AsyncTask;

import com.topolyai.avest.Bootstrap;

import java.util.List;

/**
 * Created by geri on 7/5/2015.
 */
public abstract class VestAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private Bootstrap bootstrap;

    public VestAsyncTask() {
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, false);
    }

    protected Bootstrap getBootstrap() {
        return bootstrap;
    }
}
