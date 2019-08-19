package com.eugene.aop.bindview.apt.provider;

import android.content.Context;
import android.view.View;

public class ViewProvider implements Provider {
    @Override
    public Context getContext(Object source) {
        return ((View) source).getContext();
    }

    @Override
    public View findView(Object source, int id) {
        return ((View) source).findViewById(id);
    }
}
