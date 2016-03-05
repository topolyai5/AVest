package com.topolyai.avest.android;

import android.app.IntentService;
import android.content.Intent;

public abstract class VestIntentService extends IntentService {

    public VestIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DefaultBootstrap.get().registerObject(this);
    }

}
