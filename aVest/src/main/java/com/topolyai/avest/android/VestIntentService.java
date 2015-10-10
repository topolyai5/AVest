package com.topolyai.avest.android;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.topolyai.avest.Bootstrap;

public abstract class VestIntentService extends IntentService {

    public VestIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DefaultBootstrap.get().registerObject(this);
    }

}
