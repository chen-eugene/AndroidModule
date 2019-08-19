package com.eugene.aptcompiler.router.resolver;

import com.eugene.aptannotation.router.Param;
import com.sun.tools.javac.code.Symbol;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class ParamResolver {

    private VariableElement mParamElement;
    private String mKey;

    public ParamResolver(Element e) {
        if (e.getKind() != ElementKind.PARAMETER) {
            throw new IllegalArgumentException(
                    String.format("Only parameter can be annotated with @%s", Param.class.getSimpleName()));
        }

        mParamElement = (VariableElement) e;

        mKey = mParamElement.getAnnotation(Param.class).value();

        if (mKey.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for param %s is not valid !", Param.class.getSimpleName(),
                            mParamElement.getSimpleName())
            );
        }

    }

    /**
     * Intent传递的key
     *
     * @return
     */
    public String getKey() {
        return mKey;
    }

    /**
     * 获取参数类型
     *
     * @return
     */
    public TypeMirror getParameterType() {
        return mParamElement.asType();
    }

    /**
     * 获取参数所在的方法名
     *
     * @return
     */
    public Name getOwner() {
        return mParamElement.getEnclosingElement().getSimpleName();
    }

    /**
     * 参数的名字
     *
     * @return
     */
    public Name getName() {
        return mParamElement.getSimpleName();
    }

    public String getType() {
        return ((Symbol.VarSymbol) mParamElement).type.toString();
    }

    public String getTsymType() {
        return ((Symbol.VarSymbol) mParamElement).type.tsym.toString();
    }
}
