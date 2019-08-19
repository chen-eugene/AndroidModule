package com.eugene.aop

import android.app.Activity
import android.os.Bundle
import com.eugene.aop.router.RouterInjector
import com.eugene.aptannotation.router.Extra
import com.eugene.aptannotation.router.ToThis

@ToThis(requestCode = 1)
class AopActivity : Activity() {

    @Extra
    var str: String? = null

    @Extra
    var entity: Entity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RouterInjector.inject(this)
        //        Log.d("AopActivity", "str-->" + str);
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


}
