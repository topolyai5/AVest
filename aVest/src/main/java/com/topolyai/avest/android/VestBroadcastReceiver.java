package com.topolyai.avest.android;

import com.topolyai.avest.Bootstrap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class VestBroadcastReceiver extends BroadcastReceiver {

    private Bootstrap bootstrap;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            bootstrap = DefaultBootstrap.get();
            bootstrap.registerObject(this, false);
        } catch (Exception e) {
            //ignore
        }
        receive(context, intent);
    }

    protected abstract void receive(Context context, Intent intent);

    public Bootstrap getBootstrap() {
        return bootstrap;
    }
}
