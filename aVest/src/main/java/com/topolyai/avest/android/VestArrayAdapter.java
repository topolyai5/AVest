package com.topolyai.avest.android;

import java.util.List;

import com.topolyai.avest.Bootstrap;

import android.content.Context;
import android.widget.ArrayAdapter;

public class VestArrayAdapter<T> extends ArrayAdapter<T> {

    private Bootstrap bootstrap;
    private int resId;

    public VestArrayAdapter(Context context, int resId, List<T> list) {
        super(context, resId, list);
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, false);
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }

    protected Bootstrap getBootstrap() {
        return bootstrap;
    }

}
