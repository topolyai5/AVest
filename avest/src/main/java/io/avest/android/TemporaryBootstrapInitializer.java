package io.avest.android;

import android.content.Context;

import io.avest.Bootstrap;

public class TemporaryBootstrapInitializer {

    public static Bootstrap create(Context context, boolean resolveViews) {
        return DefaultBootstrap.temporaryContext(context, resolveViews);
    }
}
