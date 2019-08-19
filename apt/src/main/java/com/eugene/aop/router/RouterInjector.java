package com.eugene.aop.router;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class RouterInjector {

    private static final Map<String, WeakReference<Resolver>> INJECTOR_MAP = new HashMap<>();

    public static void inject(Activity activity) {
        injector(activity);
    }

    private static void injector(Object host) {
        String className = host.getClass().getName();
        try {
            WeakReference<Resolver> reference = INJECTOR_MAP.get(className);
            Resolver resolver;
            if (reference != null) {
                resolver = reference.get();
                if (resolver == null) {
                    resolver = createResolver(className);
                }
            } else {
                resolver = createResolver(className);
            }
            resolver.resolve(host);
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }

    private static Resolver createResolver(String className) throws Exception {
        Class<?> injectorClass = Class.forName(className + "$Resolver");
        Resolver resolver = (Resolver) injectorClass.newInstance();
        INJECTOR_MAP.remove(className);
        INJECTOR_MAP.put(className, new WeakReference<>(resolver));
        return resolver;
    }

}
