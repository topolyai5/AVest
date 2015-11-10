package com.topolyai.avest.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.topolyai.avest.Bootstrap;

public class VestAppCompatActivity extends AppCompatActivity {

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
