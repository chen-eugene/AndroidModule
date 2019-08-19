package com.eugene.aptcompiler.utils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.Map;

public class ResolveUtil {

    private static AnnotationMirror getAnnotationMirror(ExecutableElement element, Class<?> clazz) {
        String clazzName = clazz.getName();
        for (AnnotationMirror m : element.getAnnotationMirrors()) {
            if (m.getAnnotationType().toString().equals(clazzName)) {
                return m;
            }
        }
        return null;
    }

    private static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String key) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }


    public static TypeMirror getAnnotationValue(ExecutableElement foo, Class<?> clazz, String key) {
        AnnotationMirror am = getAnnotationMirror(foo, clazz);
        if (am == null) {
            return null;
        }
        AnnotationValue av = getAnnotationValue(am, key);
        if (av == null) {
            return null;
        } else {
            return (TypeMirror) av.getValue();
        }
    }


}
