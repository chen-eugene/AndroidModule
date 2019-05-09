package com.eugene.aop.apt.processor;

import com.example.aop.annotation.BindView;
import com.example.aop.apt.annotation.Bind;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class BindField {

    private VariableElement mFieldElement;
    private int mResId;

    public BindField(Element element) throws IllegalArgumentException {

        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", Bind.class.getSimpleName()));
        }


        mFieldElement = (VariableElement) element;
        Bind bind = mFieldElement.getAnnotation(Bind.class);
        mResId = bind.value();
        if (mResId < 0) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for field %s is not valid !", BindView.class.getSimpleName(),
                            mFieldElement.getSimpleName())
            );
        }

    }


    public Name getFieldName() {
        return mFieldElement.getSimpleName();
    }

    public int getResId() {
        return mResId;
    }

    public TypeMirror getFieldType() {
        return mFieldElement.asType();
    }


}
