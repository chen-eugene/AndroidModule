package com.eugene.aop;

import android.app.Activity;
import com.eugene.aptannotation.router.Param;
import com.eugene.aptannotation.router.To;
import com.eugene.aptannotation.router.ToForResult;

import java.util.List;

public interface RouterHelper {

    @To(AopActivity.class)
    public void startTarget(Activity from, @Param("str") String str);

    @ToForResult(to = AopActivity.class, requestCode = 1)
    public void start(Activity from, @Param("data") List<String> data);


}
