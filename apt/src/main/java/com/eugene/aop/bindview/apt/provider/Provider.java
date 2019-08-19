package com.eugene.aop.bindview.apt.provider;

import android.content.Context;
import android.view.View;

public interface Provider {

    Context getContext(Object source);

    View findView(Object source, int id);


}
