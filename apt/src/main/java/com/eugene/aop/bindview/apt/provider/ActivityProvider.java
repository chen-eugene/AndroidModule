package com.eugene.aop.bindview.apt.provider;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class ActivityProvider implements Provider {
    @Override
    public Context getContext(Object source) {
        return (Activity) source;
    }

    @Override
    public View findView(Object source, int id) {
        return ((Activity) source).findViewById(id);
    }

}
