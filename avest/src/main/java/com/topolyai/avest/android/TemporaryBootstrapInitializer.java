package com.topolyai.avest.android;

import android.content.Context;

import com.topolyai.avest.Bootstrap;

public class TemporaryBootstrapInitializer {

    public static Bootstrap create(Context context, boolean resolveViews) {
        return DefaultBootstrap.temporaryContext(context, resolveViews);
    }
}
