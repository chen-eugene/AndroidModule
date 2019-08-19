package com.eugene.aptcompiler.bindview;

import com.eugene.aptcompiler.utils.TypeUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;

/**
 * AnnotatedClass 表示一个注解类，里面放了两个列表，分别装着注解的成员变量和方法。
 * 在 generateFinder() 方法中，按照上一节设计的模板，利用 JavaPoet 的 API 生成代码。
 */
public class AnnotatedClass {

    public TypeElement mClassElement;
    public List<BindField> mFields;
    public List<OnClickMethod> mMethods;
    public Elements mElementUtils;

    public AnnotatedClass(TypeElement classElement, Elements elementUtils) {
        this.mClassElement = classElement;
        this.mFields = new ArrayList<>();
        this.mMethods = new ArrayList<>();
        this.mElementUtils = elementUtils;
    }

    public String getFullClassName() {
        return mClassElement.getQualifiedName().toString();
    }

    public void addField(BindField field) {
        mFields.add(field);
    }

    public void addMethod(OnClickMethod method) {
        mMethods.add(method);
    }

    public JavaFile generateBinder() {

        MethodSpec.Builder bindMethodBuilder = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "host", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(TypeUtil.PROVIDER, "provider");

        for (BindField field : mFields) {
            bindMethodBuilder.addStatement("host.$N = ($T)(provider.findView(source, $L))", field.getFieldName(),
                    ClassName.get(field.getFieldType()), field.getResId());
        }

        if (mMethods.size() > 0) {
            bindMethodBuilder.addStatement("$T listener", TypeUtil.ANDROID_ON_CLICK_LISTENER);
        }

        for (OnClickMethod method : mMethods) {
            TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(TypeUtil.ANDROID_ON_CLICK_LISTENER)
                    .addMethod(MethodSpec.methodBuilder("onClick")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .addParameter(TypeUtil.ANDROID_VIEW, "view")
                            .addStatement("host.$N()", method.getMethodName())
                            .build())
                    .build();

            bindMethodBuilder.addStatement("listener = $L ", listener);

            for (int id : method.mIds) {
                bindMethodBuilder.addStatement("provider.findView(source, $L).setOnClickListener(listener)", id);
            }
        }

        TypeSpec binderClass = TypeSpec.classBuilder(mClassElement.getSimpleName() + "$$Binder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.BINDER, TypeName.get(mClassElement.asType())))
                .addMethod(bindMethodBuilder.build())
                .build();

        String packageName = mElementUtils.getPackageOf(mClassElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, binderClass).build();
    }


}
