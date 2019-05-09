package com.eugene.aop.apt.processor;

import com.example.aop.apt.annotation.OnClick;

import javax.lang.model.element.*;
import java.util.List;

public class OnClickMethod {

    private ExecutableElement mMethodElement;
    private Name mMethodName;
    public int[] mIds;

    public OnClickMethod(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.METHOD)
            throw new IllegalArgumentException(
                    String.format("Only methods can be annotated with @%s", OnClick.class.getSimpleName()));

        this.mMethodElement = (ExecutableElement) element;
        this.mIds = mMethodElement.getAnnotation(OnClick.class).value();

        if (mIds == null) {
            throw new IllegalArgumentException(String.format("Must set valid ids for @%s", OnClick.class.getSimpleName()));
        } else {
            for (int id : mIds) {
                if (id < 0)
                    throw new IllegalArgumentException(String.format("Must set valid id for @%s", OnClick.class.getSimpleName()));
            }
        }

        this.mMethodName = mMethodElement.getSimpleName();
        List<? extends VariableElement> parameters = mMethodElement.getParameters();
        if (parameters.size() > 0) {
            throw new IllegalArgumentException(
                    String.format("The method annotated with @%s must have no parameters", OnClick.class.getSimpleName()));
        }

    }

    public Name getMethodName() {
        return mMethodName;
    }

}
