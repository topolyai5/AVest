package io.avest.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.avest.Bootstrap;

public class VestFragment extends Fragment {

    private Bootstrap bootstrap;
    private View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, true);
        return layout;
    }

    public View getLayout() {
        return layout;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            hide();
        } else {
            show();
        }
        super.onHiddenChanged(hidden);
    }

    protected void hide() {

    }

    protected void show() {

    }

}
