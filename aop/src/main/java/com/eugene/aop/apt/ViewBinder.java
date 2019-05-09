package com.eugene.aop.apt;

import android.app.Activity;
import android.view.View;
import com.example.aop.apt.provider.ActivityProvider;
import com.example.aop.apt.provider.Provider;
import com.example.aop.apt.provider.ViewProvider;

import java.util.HashMap;
import java.util.Map;

public class ViewBinder {

    private static final ActivityProvider PROVIDER_ACTIVITY = new ActivityProvider();
    private static final ViewProvider PROVIDER_VIEW = new ViewProvider();

    private static final Map<String, Binder> BINDER_MAP = new HashMap<>();

    public static void bind(Activity activity) {
        bind(activity, activity, PROVIDER_ACTIVITY);
    }

    public static void bind(View view) {
        bind(view, view);
    }

    public static void bind(Object host, View view) {
        bind(host, view, PROVIDER_VIEW);
    }

    private static void bind(Object host, Object source, Provider provider) {
        String className = host.getClass().getName();

        try {
            Binder binder = BINDER_MAP.get(className);
            if (binder == null) {
                Class binderClass = Class.forName(className + "$Binder");
                binder = (Binder) binderClass.newInstance();
                BINDER_MAP.put(className, binder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
