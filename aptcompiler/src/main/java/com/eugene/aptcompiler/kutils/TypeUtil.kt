package com.eugene.aptcompiler.kutils

import com.squareup.kotlinpoet.ClassName


val ANDROID_VIEW = ClassName.bestGuess("android.view.View")
val ANDROID_ON_CLICK_LISTENER = ClassName.bestGuess("android.view.View.OnClickListener")
val BINDER = ClassName.bestGuess("com.example.aop.apt.Binder")
val PROVIDER = ClassName.bestGuess("com.example.aop.apt.provider.Provider")

val RESOLVER = ClassName.bestGuess("com.eugene.aop.router.Resolver")
val ACTIVITY = ClassName.bestGuess("android.app.Activity")
val INTENT = ClassName.bestGuess("android.content.Intent")
val BUNDLE = ClassName.bestGuess("android.os.Bundle")
