package com.eugene.aptcompiler.router;

import com.eugene.aptcompiler.router.resolver.ToThisResolver;
import com.eugene.aptcompiler.utils.TypeUtil;
import com.eugene.aptcompiler.router.resolver.ExtraResolver;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;

public class RouterHelper {

    private Elements mElementUtils;
    private TypeElement mClassElement;
    private ToThisResolver mToThisResolver;
    private List<ExtraResolver> mExtras = new ArrayList<>();

    public RouterHelper(Elements mElementUtils, TypeElement classElement) {
        this.mElementUtils = mElementUtils;
        this.mClassElement = classElement;
    }

    public JavaFile generateResolve() {

        //生成startActivity方法
        MethodSpec startMethod = createStartActivity();

        //生成startActivity方法
        MethodSpec startForResultMethod = createStartActivityForResult();

        //生成resolve方法
        MethodSpec resolveMethod = createResolve();

        TypeSpec.Builder classBuild = TypeSpec.classBuilder(mClassElement.getSimpleName() + "$Router")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.RESOLVER, TypeName.get(mClassElement.asType())))
                .addMethod(startMethod)
                .addMethod(resolveMethod);

        if (startForResultMethod != null) {
            classBuild.addMethod(startForResultMethod);

        }

        TypeSpec resolverClass = classBuild.build();

        String packageName = mElementUtils.getPackageOf(mClassElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, resolverClass).build();
    }

    private MethodSpec createStartActivity() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("startActivity")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID);

        resolveData(builder);

        builder.addStatement("$L.startActivity(i)", "context");

        return builder.build();
    }

    private MethodSpec createStartActivityForResult() {
        if (mToThisResolver.getCode() < 0)
            return null;

        MethodSpec.Builder builder = MethodSpec.methodBuilder("startActivityForResult")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID);

        resolveData(builder);

        builder.addStatement("$L.startActivityForResult(i,$L)", "context", mToThisResolver.getCode());

        return builder.build();
    }

    private MethodSpec createResolve() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("resolve")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(TypeName.get(mClassElement.asType()), "host", Modifier.FINAL)
                .addStatement("$T i = host.getIntent()", TypeUtil.INTENT)
                .addStatement("$T data = i.getExtras()", TypeUtil.BUNDLE);

        for (ExtraResolver extraResolver : mExtras) {
            builder.addStatement("host.$L = ($T)data.get($S)", extraResolver.getExtraName(),
                    TypeName.get(extraResolver.getExtraType()), extraResolver.getKey());
        }

        return builder.build();
    }


    private void resolveData(MethodSpec.Builder builder) {
        //添加context参数
        builder.addParameter(TypeUtil.ACTIVITY, "context", Modifier.FINAL);

        //添加参数
        for (ExtraResolver resolver : mExtras) {
            builder.addParameter(TypeName.get(resolver.getExtraType()), resolver.getExtraName().toString());
        }

        builder.addStatement("$T i = new $T($L,$T.class)", TypeUtil.INTENT, TypeUtil.INTENT,
                "context", TypeName.get(mClassElement.asType()));

        //put数据
        for (ExtraResolver resolver : mExtras) {
            verifyParam(resolver, builder);
        }

    }

    private void verifyParam(ExtraResolver resolver, MethodSpec.Builder builder) {
        if ("java.util.List".equals(resolver.getTsymType())) {
            switch (resolver.getType()) {
                case "java.util.List<java.lang.String>":
                    builder.addStatement("i.putStringArrayListExtra($S,($T<$T>)$L)", resolver.getKey(), ArrayList.class,
                            String.class, resolver.getExtraName());
                    break;
                case "java.util.List<java.lang.Integer>":
                    builder.addStatement("i.putIntegerArrayListExtra($S,($T<$T>)$L)", resolver.getKey(), ArrayList.class,
                            Integer.class, resolver.getExtraName());
                    break;
                default:
                    builder.addStatement("i.putParcelableArrayListExtra($S,$L)", resolver.getKey(), resolver.getExtraName());
                    break;
            }
        } else {
            builder.addStatement("i.putExtra($S,$L)", resolver.getKey(), resolver.getExtraName());
        }
    }


    public void addField(ExtraResolver extraResolver) {
        mExtras.add(extraResolver);
    }

    public void setToThisResolver(ToThisResolver resolver) {
        this.mToThisResolver = resolver;
    }

}
