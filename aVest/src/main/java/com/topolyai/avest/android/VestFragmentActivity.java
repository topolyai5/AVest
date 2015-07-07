package com.topolyai.avest.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.topolyai.avest.Bootstrap;

public class VestFragmentActivity extends FragmentActivity {

    private Bootstrap bootstrap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bootstrap = DefaultBootstrap.create(this);
    }

    protected Bootstrap getBootstrap() {
        return bootstrap;
    }
}
