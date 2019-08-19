package com.eugene.aptcompiler.krouter.resolver

import com.eugene.aptannotation.router.Extra
import com.eugene.aptcompiler.kutils.javaToKotlinType
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import com.sun.tools.javac.code.Symbol
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Name
import javax.lang.model.element.VariableElement

class ExtraResolver(e: Element) {

    private val mExtraElement: VariableElement

    init {
        if (e.kind != ElementKind.FIELD) {
            throw IllegalArgumentException(
                String.format("Only parameter can be annotated with @%s", Extra::class.java.simpleName)
            )
        }

        mExtraElement = e as VariableElement
    }

    /**
     * Intent传递的key
     */
    fun getKey(): String {
        return mExtraElement.simpleName.toString()
    }

    /**
     * 获取参数类型
     */
    fun getExtraType(): TypeName {
        return mExtraElement.javaToKotlinType() ?: mExtraElement.asType().asTypeName()
//        return mExtraElement.asType()
    }

    /**
     * 参数的名字
     */
    fun getExtraName(): Name {
        return mExtraElement.simpleName
    }

    fun getType(): String {
        return (mExtraElement as Symbol.VarSymbol).type.toString()
    }

    fun getTsymType(): String {
        return (mExtraElement as Symbol.VarSymbol).type.tsym.toString()
    }
}
