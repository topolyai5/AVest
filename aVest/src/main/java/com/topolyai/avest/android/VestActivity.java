package com.topolyai.avest.android;

import android.app.Activity;
import android.os.Bundle;

import com.topolyai.avest.Bootstrap;

public class VestActivity extends Activity {

    private Bootstrap bootstrap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bootstrap = DefaultBootstrap.create(this);
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }
}
