package com.eugene.aptcompiler.utils;

import com.squareup.javapoet.ClassName;

public class TypeUtil {
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName BINDER = ClassName.get("com.example.aop.apt", "Binder");
    public static final ClassName PROVIDER = ClassName.get("com.example.aop.apt.provider", "Provider");

    public static final ClassName RESOLVER = ClassName.get("com.eugene.aop.router", "Resolver");
    public static final ClassName ACTIVITY = ClassName.get("android.app", "Activity");
    public static final ClassName INTENT = ClassName.get("android.content", "Intent");
    public static final ClassName BUNDLE = ClassName.get("android.os", "Bundle");
}
