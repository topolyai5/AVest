package com.topolyai.avest.android;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.topolyai.avest.Bootstrap;

public class VestDialogFragment extends DialogFragment {

    private Bootstrap bootstrap;
    private View layout;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bootstrap = DefaultBootstrap.get();
        bootstrap.registerObject(this, true);

        AlertDialog.Builder builder = new Builder(getActivity());
        builder.setView(layout);
        return builder.create();
    }

    public View getLayout() {
        return layout;
    }

    protected Bootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }
}
