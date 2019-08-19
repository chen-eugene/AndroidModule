package com.eugene.aptcompiler.router.resolver;

import com.eugene.aptannotation.router.ToThis;
import com.sun.tools.javac.code.Symbol;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

public class ToThisResolver {

    private TypeElement mElement;
    private int mCode;

    public ToThisResolver(Element e) {
        if (e.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException(
                    String.format("Only class can be annotated with @%s", ToThis.class.getSimpleName()));
        }

        mElement = (TypeElement) e;

        mCode = mElement.getAnnotation(ToThis.class).requestCode();
    }

    /**
     * Intent传递的key
     *
     * @return
     */
    public int getCode() {
        return mCode;
    }

    /**
     * 获取参数类型
     *
     * @return
     */
    public TypeMirror getExtraType() {
        return mElement.asType();
    }

    /**
     * 参数的名字
     *
     * @return
     */
    public Name getExtraName() {
        return mElement.getSimpleName();
    }

    public String getType() {
        return ((Symbol.VarSymbol) mElement).type.toString();
    }

    public String getTsymType() {
        return ((Symbol.VarSymbol) mElement).type.tsym.toString();
    }
}
