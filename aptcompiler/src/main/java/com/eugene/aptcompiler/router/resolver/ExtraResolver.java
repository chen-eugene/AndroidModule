package com.eugene.aptcompiler.router.resolver;

import com.eugene.aptannotation.router.Extra;
import com.sun.tools.javac.code.Symbol;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class ExtraResolver {

    private VariableElement mExtraElement;
    private String mKey;

    public ExtraResolver(Element e) {
        if (e.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only parameter can be annotated with @%s", Extra.class.getSimpleName()));
        }

        mExtraElement = (VariableElement) e;
    }

    /**
     * Intent传递的key
     *
     * @return
     */
    public String getKey() {
        return mExtraElement.getSimpleName().toString();
    }

    /**
     * 获取参数类型
     *
     * @return
     */
    public TypeMirror getExtraType() {
        return mExtraElement.asType();
    }

    /**
     * 参数的名字
     *
     * @return
     */
    public Name getExtraName() {
        return mExtraElement.getSimpleName();
    }

    public String getType() {
        return ((Symbol.VarSymbol) mExtraElement).type.toString();
    }

    public String getTsymType() {
        return ((Symbol.VarSymbol) mExtraElement).type.tsym.toString();
    }
}
