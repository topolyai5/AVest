package com.topolyai.avest.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.topolyai.avest.Bootstrap;

public class VestSupportMapFragment extends SupportMapFragment {

    private Bootstrap bootstrap;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, true);
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mapInited(getMap());
        return view;
    }

    protected void mapInited(GoogleMap map) {

    }
    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            hide();
        } else {
            show();
        }
        super.onHiddenChanged(hidden);
    }


    protected void hide() {

    }

    protected void show() {

    }

}
