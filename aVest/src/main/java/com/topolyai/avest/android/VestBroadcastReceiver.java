package com.topolyai.avest.android;

import com.topolyai.avest.Bootstrap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class VestBroadcastReceiver extends BroadcastReceiver {

    private Bootstrap bootstrap;

    @Override
    public void onReceive(Context context, Intent intent) {
        bootstrap = DefaultBootstrap.create(context);
        bootstrap.registerObject(this, false);
        receive(context, intent);
    }

    protected abstract void receive(Context context, Intent intent);

    public Bootstrap getBootstrap() {
        return bootstrap;
    }
}
