package com.eugene.aptcompiler.router;

import com.eugene.aptannotation.router.*;
import com.eugene.aptcompiler.router.resolver.*;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.*;

//@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {

    /**
     * 文件相关的辅助类
     */
    private Filer mFiler;
    /**
     * 元素相关的辅助类
     */
    private Elements mElementUtils;
    /**
     * 日志相关的辅助类
     */
    private Messager mMessager;

    private Map<String, RouterHelper> mRouterHelpers = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
//        mMessager.printMessage(Diagnostic.Kind.ERROR,"日志开始---------------");
    }

    /**
     * @Return 指定哪些注解应该被注解处理器注册
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(ToThis.class.getCanonicalName());
        types.add(Extra.class.getCanonicalName());
        return types;
    }

    /**
     * @return 指定使用的 Java 版本。通常返回 SourceVersion.latestSupported()。
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mRouterHelpers.clear();

        processFieldExtra(roundEnvironment);
        try {
            for (RouterHelper helper : mRouterHelpers.values()) {
                helper.generateResolve().writeTo(mFiler);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }

        return true;
    }

    private void processFieldExtra(RoundEnvironment roundEnv) {

        for (Element element : roundEnv.getElementsAnnotatedWith(ToThis.class)) {
            RouterHelper helper = createRouterHelper(element);
            ToThisResolver codeResolver = new ToThisResolver(element);
            helper.setToThisResolver(codeResolver);
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Extra.class)) {
            RouterHelper helper = getRouterHelperByExtra(element);
            ExtraResolver extra = new ExtraResolver(element);
            helper.addField(extra);
        }

    }

    private RouterHelper createRouterHelper(Element element) {
        TypeElement classElement = (TypeElement) element;
        return getRouterHelper(classElement);
    }

    private RouterHelper getRouterHelperByExtra(Element element) {
        TypeElement classElement = (TypeElement) element.getEnclosingElement();
        return getRouterHelper(classElement);
    }

    private RouterHelper getRouterHelper(TypeElement classElement) {
        String fullClassName = classElement.getQualifiedName().toString();
        RouterHelper helper = mRouterHelpers.get(fullClassName);
        if (helper == null) {
            helper = new RouterHelper(mElementUtils, classElement);
            mRouterHelpers.put(fullClassName, helper);
        }
        return helper;
    }

}
