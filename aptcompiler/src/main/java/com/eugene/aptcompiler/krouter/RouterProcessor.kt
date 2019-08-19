package com.eugene.aptcompiler.krouter

import com.eugene.aptannotation.router.Extra
import com.eugene.aptannotation.router.ToThis
import com.eugene.aptcompiler.krouter.resolver.ExtraResolver
import com.eugene.aptcompiler.krouter.resolver.ToThisResolver
import com.squareup.kotlinpoet.FileSpec
import java.io.File
import java.io.IOException
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

class RouterProcessor : AbstractProcessor() {

    /**
     * 文件相关的辅助类
     */
    private var mFiler: Filer? = null
    /**
     * 元素相关的辅助类
     */
    private var mElementUtils: Elements? = null
    /**
     * 日志相关的辅助类
     */
    private var mMessager: Messager? = null

    private val mRouterHelpers = HashMap<String, RouterHelper>()

    override fun init(processingEnvironment: ProcessingEnvironment?) {
        super.init(processingEnvironment)
        mFiler = processingEnvironment?.filer
        mElementUtils = processingEnvironment?.elementUtils
        mMessager = processingEnvironment?.messager
        mMessager?.printMessage(Diagnostic.Kind.WARNING, "日志开始---------------")
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        mMessager?.printMessage(Diagnostic.Kind.WARNING, "getSupportedAnnotationTypes---------------")
        return mutableSetOf<String>().apply {
            this.add(ToThis::class.java.canonicalName)
            this.add(Extra::class.java.canonicalName)
        }
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(set: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        mRouterHelpers.clear()
        mMessager?.printMessage(Diagnostic.Kind.WARNING, "process---------------")
        processFieldExtra(roundEnvironment)

        for (helper in mRouterHelpers.values) {
            helper.generateResolve().writeTo(mFiler!!)
        }

        return true
    }

    private fun processFieldExtra(roundEnv: RoundEnvironment?) {

        roundEnv?.getElementsAnnotatedWith(ToThis::class.java)
            ?.forEach {
                val helper = createRouterHelper(it)
                val codeResolver = ToThisResolver(it)
                helper.setToThisResolver(codeResolver)
            }

        roundEnv?.getElementsAnnotatedWith(Extra::class.java)
            ?.forEach {
                val helper = getRouterHelperByExtra(it)
                val extra = ExtraResolver(it)
                helper.addField(extra)
            }
    }

    private fun createRouterHelper(element: Element): RouterHelper {
        val classElement = element as TypeElement
        return getRouterHelper(classElement)
    }

    private fun getRouterHelperByExtra(element: Element): RouterHelper {
        val classElement = element.enclosingElement as TypeElement
        return getRouterHelper(classElement)
    }

    private fun getRouterHelper(classElement: TypeElement): RouterHelper {
        val fullClassName = classElement.qualifiedName.toString()
        var helper = mRouterHelpers[fullClassName]
        if (helper == null) {
            helper = RouterHelper(mElementUtils!!, classElement)
            mRouterHelpers[fullClassName] = helper
        }
        return helper
    }


}
