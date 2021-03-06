package io.avest.android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import io.avest.Bootstrap;

public class VestFragmentActivity extends FragmentActivity {

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
