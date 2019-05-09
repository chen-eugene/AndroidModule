package com.example.core.utils.kotlin

import android.os.Build

class VersionUtil {

    companion object {
        val isAfter26: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

        val isAfter25: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1

        val isAfter24: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

        val isAfter23: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

        val isAfter22: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1

        val isAfter21: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

        val isAfter20: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH

        val isAfter19: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        val isAfter18: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2

        val isAfter17: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1

        val isAfter16: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN

    }

}