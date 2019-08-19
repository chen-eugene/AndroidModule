package com.eugene.aptcompiler.router;

import com.eugene.aptcompiler.utils.TypeUtil;
import com.eugene.aptcompiler.router.resolver.ParamResolver;
import com.eugene.aptcompiler.router.resolver.ToForResultResolver;
import com.eugene.aptcompiler.router.resolver.ToResolver;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.classfile.Type;
import com.sun.tools.javac.code.Symbol;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * AnnotatedClass 表示一个注解类，里面放了两个列表，分别装着注解的成员变量和方法。
 * 在 generateFinder() 方法中，按照上一节设计的模板，利用 JavaPoet 的 API 生成代码。
 */
public class ClassHelper {

    private Elements mElementUtils;
    private Element mClassElement;

    private Map<ToResolver, List<ParamResolver>> mToMethods = new LinkedHashMap<>();

    private Map<ToForResultResolver, List<ParamResolver>> mToForResultMethods = new LinkedHashMap<>();

    public ClassHelper(Elements mElementUtils, Element classElement) {
        this.mElementUtils = mElementUtils;
        this.mClassElement = classElement;
    }

    public JavaFile generateProcess() {

        //生成Router工具类
        TypeSpec.Builder helperClassBuilder = TypeSpec.classBuilder("Router")
                .addModifiers(Modifier.PUBLIC);

        resolveTo(helperClassBuilder);

        resolveToForResult(helperClassBuilder);

//        String packageName = mElementUtils.getPackageOf(mClassElement).getQualifiedName().toString();

        return JavaFile.builder("com.router", helperClassBuilder.build()).build();
    }

    /**
     * 处理startActivity
     *
     * @param classBuilder
     */
    private void resolveTo(TypeSpec.Builder classBuilder) {
        //添加方法
        for (ToResolver toResolver : mToMethods.keySet()) {

            MethodSpec.Builder toBuilder = MethodSpec.methodBuilder(toResolver.getMethodName())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID);

            toBuilder.addParameter(TypeName.get(toResolver.getFromType()), toResolver.getFromName().toString());

            //添加参数
            for (ParamResolver paramResolver : mToMethods.get(toResolver)) {
                toBuilder.addParameter(TypeName.get(paramResolver.getParameterType()), paramResolver.getName().toString());
            }

            toBuilder.addStatement("$T i = new $T($L,$T.class)", TypeUtil.INTENT, TypeUtil.INTENT,
                    toResolver.getFromName(), TypeName.get(toResolver.getTo()));

            for (ParamResolver paramResolver : mToMethods.get(toResolver)) {
                resolveParam(paramResolver, toBuilder);
            }

            toBuilder.addStatement("$L.startActivity(i)", toResolver.getFromName());

            classBuilder.addMethod(toBuilder.build());
        }
    }

    /**
     * 处理startActivityForResult
     *
     * @param classBuilder
     */
    private void resolveToForResult(TypeSpec.Builder classBuilder) {
        //添加方法
        for (ToForResultResolver resolver : mToForResultMethods.keySet()) {

            MethodSpec.Builder toBuilder = MethodSpec.methodBuilder(resolver.getMethodName())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID);

            toBuilder.addParameter(TypeName.get(resolver.getFromType()), resolver.getFromName().toString());

            //添加参数
            for (ParamResolver paramResolver : mToForResultMethods.get(resolver)) {
                toBuilder.addParameter(TypeName.get(paramResolver.getParameterType()), paramResolver.getName().toString());
            }

            toBuilder.addStatement("$T i = new $T($L,$T.class)", TypeUtil.INTENT, TypeUtil.INTENT,
                    resolver.getFromName(), TypeName.get(resolver.getTo()));

            for (ParamResolver paramResolver : mToForResultMethods.get(resolver)) {
                resolveParam(paramResolver, toBuilder);
            }

            toBuilder.addStatement("$L.startActivityForResult(i,$L)", resolver.getFromName(), resolver.getRequestCode());

            classBuilder.addMethod(toBuilder.build());
        }
    }

    private void resolveParam(ParamResolver paramResolver, MethodSpec.Builder builder) {
        if ("java.util.List".equals(paramResolver.getTsymType())) {
            switch (paramResolver.getType()) {
                case "java.util.List<java.lang.String>":
                    builder.addStatement("i.putStringArrayListExtra($S,($T<$T>)$L)", paramResolver.getKey(), ArrayList.class,
                            String.class, paramResolver.getName());
                    break;
                case "java.util.List<java.lang.Integer>":
                    builder.addStatement("i.putIntegerArrayListExtra($S,($T<$T>)$L)", paramResolver.getKey(), ArrayList.class,
                            Integer.class, paramResolver.getName());
                    break;
                default:
                    builder.addStatement("i.putParcelableArrayListExtra($S,$L)", paramResolver.getKey(), paramResolver.getName());
                    break;
            }
        } else {
            builder.addStatement("i.putExtra($S,$L)", paramResolver.getKey(), paramResolver.getName());
        }
    }

    public void addTo(ToResolver resolver) {
        mToMethods.put(resolver, new ArrayList<>());
    }

    public void addToForResult(ToForResultResolver resolver) {
        mToForResultMethods.put(resolver, new ArrayList<>());
    }

    public void addExtra(ParamResolver extraElement) {
        for (ToResolver resolver : mToMethods.keySet()) {
            if (resolver.getMethodName().equals(extraElement.getOwner().toString())) {
                mToMethods.get(resolver).add(extraElement);
                return;
            }
        }
        for (ToForResultResolver resolver : mToForResultMethods.keySet()) {
            if (resolver.getMethodName().equals(extraElement.getOwner().toString())) {
                mToForResultMethods.get(resolver).add(extraElement);
                return;
            }
        }
    }


}
