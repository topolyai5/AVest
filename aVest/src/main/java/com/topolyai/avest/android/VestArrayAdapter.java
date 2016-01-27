package com.topolyai.avest.android;

import java.util.List;

import com.topolyai.avest.Bootstrap;
import com.topolyai.avest.annotations.Inject;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class VestArrayAdapter<T> extends ArrayAdapter<T> {

    @Inject
    protected LayoutInflater inflater;

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

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(getResId(), null);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
