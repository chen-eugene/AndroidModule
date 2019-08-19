package com.eugene.aptcompiler.krouter

import com.eugene.aptcompiler.krouter.resolver.ExtraResolver
import com.eugene.aptcompiler.krouter.resolver.ToThisResolver
import com.eugene.aptcompiler.kutils.ACTIVITY
import com.eugene.aptcompiler.kutils.INTENT
import com.eugene.aptcompiler.kutils.RESOLVER
import com.eugene.aptcompiler.utils.TypeUtil
import com.squareup.kotlinpoet.*
import java.lang.reflect.ParameterizedType
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import kotlin.reflect.KClass


class RouterHelper(private val mElementUtils: Elements, private val mClassElement: TypeElement) {

    private var mToThisResolver: ToThisResolver? = null

    private val mExtras = mutableListOf<ExtraResolver>()

    fun generateResolve(): FileSpec {

        //生成startActivity方法
        val startMethod = createStartActivity()

        //生成startActivity方法
        val startForResultMethod = createStartActivityForResult()

        //生成resolve方法
        val resolveMethod = createResolve()

        //生成companion类
        val companionBuilder = TypeSpec.companionObjectBuilder()
            .addFunction(startMethod)

        if (startForResultMethod != null) {
            companionBuilder.addFunction(startForResultMethod)
        }

        //生成工具类
        val classSpec = TypeSpec.classBuilder(mClassElement.simpleName.toString() + "_Router")
//            .addModifiers(Modifier.PUBLIC)
//            .addSuperinterface(ParameterizedTypeName.get(TypeUtil.RESOLVER, TypeName.get(mClassElement.asType())))
//            .addMethod(startMethod)
//            .addMethod(resolveMethod)


            .addSuperinterface(RESOLVER)
            .addFunction(resolveMethod)
            .addType(companionBuilder.build())
            .build()

//        Any::class.asClassName().param

        val packageName = mElementUtils.getPackageOf(mClassElement).qualifiedName.toString()

        return FileSpec.builder(packageName, mClassElement.simpleName.toString() + "_Router")
            .addType(classSpec)
            .build()
    }

    private fun createStartActivity(): FunSpec {
        val builder = FunSpec.builder("startActivity")

        resolveData(builder)

        builder.addStatement("%L.startActivity(i)", "context")

        return builder.build()
    }

    private fun createStartActivityForResult(): FunSpec? {
        if (mToThisResolver?.getCode() == null || mToThisResolver!!.getCode() < 0)
            return null

        val builder = FunSpec.builder("startActivityForResult")

        resolveData(builder)

        builder.addStatement("%L.startActivityForResult(i,%L)", "context", mToThisResolver!!.getCode())

        return builder.build()
    }

    private fun createResolve(): FunSpec {
        val builder = FunSpec.builder("resolve")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("host", mClassElement.asClassName())
            .addStatement("val i = host.getIntent()", TypeUtil.INTENT)
            .addStatement("val data = i?.getExtras()", TypeUtil.BUNDLE)

        mExtras.forEach {
            builder.addStatement("data?.get(%S)?.let{", it.getKey())
            builder.addStatement("host.%L = it as %T }", it.getExtraName(), it.getExtraType())
        }

        if (mExtras.isNotEmpty()) {

        }
        for (extraResolver in mExtras) {

        }

        return builder.build()
    }


    private fun resolveData(builder: FunSpec.Builder) {
        //添加context参数
        builder.addParameter("context", ACTIVITY)

        //添加参数
        for (resolver in mExtras) {
            builder.addParameter(resolver.getExtraName().toString(), resolver.getExtraType())
        }

        builder.addStatement(
            "val i = %T(%L,%T::class.java)", INTENT, "context", mClassElement.asType()
        )

        //put数据
        for (resolver in mExtras) {
            verifyParam(resolver, builder)
        }

    }

    private fun verifyParam(resolver: ExtraResolver, builder: FunSpec.Builder) {
        if ("java.util.List" == resolver.getTsymType()) {
//            when (resolver.getType()) {
//                "java.util.List<java.lang.String>" -> builder.addStatement(
//                    "i.putStringArrayListExtra(%S,(%T<%T>)%L)", resolver.getKey(), ArrayList<*>::class.java,
//                    String::class.java, resolver.extraName
//                )
//                "java.util.List<java.lang.Integer>" -> builder.addStatement(
//                    "i.putIntegerArrayListExtra(\$S,(\$T<\$T>)\$L)", resolver.key, ArrayList<*>::class.java,
//                    Int::class.java, resolver.extraName
//                )
//                else -> builder.addStatement("i.putParcelableArrayListExtra(\$S,\$L)", resolver.key, resolver.extraName)
//            }
        } else {
            builder.addStatement("i.putExtra(%S,%L)", resolver.getKey(), resolver.getExtraName())
        }
    }


    fun addField(extraResolver: ExtraResolver) {
        mExtras.add(extraResolver)
    }

    fun setToThisResolver(resolver: ToThisResolver) {
        this.mToThisResolver = resolver
    }

}
