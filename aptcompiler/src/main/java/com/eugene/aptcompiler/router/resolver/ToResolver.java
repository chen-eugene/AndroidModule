package com.eugene.aptcompiler.router.resolver;

import com.eugene.aptannotation.router.To;
import com.eugene.aptcompiler.utils.ResolveUtil;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public class ToResolver {

    private ExecutableElement mElement;
    private VariableElement mFrom;
    private TypeMirror mTo;

    public ToResolver(Element e) {
        if (e.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(
                    String.format("Only methods can be annotated with @%s", To.class.getSimpleName()));
        }

        this.mElement = (ExecutableElement) e;


        mTo = ResolveUtil.getAnnotationValue(mElement, To.class, "value");

        //判断参数
        List<? extends VariableElement> parameters = mElement.getParameters();
        if (parameters.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("The method is annotated with @%s must have one parameter at least", To.class.getSimpleName())
            );
        }

        mFrom = parameters.get(0);
    }

    public String getMethodName() {
        return mElement.getSimpleName().toString();
    }

    public TypeMirror getFromType() {
        return mFrom.asType();
    }

    public Name getFromName() {
        return mFrom.getSimpleName();
    }

    public TypeMirror getTo() {
        return mTo;
    }

    public TypeElement getClassElement() {
        return (TypeElement) (mElement.getEnclosingElement());
    }
}
