package com.eugene.aptcompiler.krouter.resolver

import com.eugene.aptannotation.router.ToThis
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

class ToThisResolver(e: Element) {

    private val mElement: TypeElement

    init {
        if (e.kind != ElementKind.CLASS) {
            throw IllegalArgumentException(
                String.format("Only class can be annotated with @%s", ToThis::class.java.simpleName)
            )
        }

        mElement = e as TypeElement
    }

    /**
     * requestCode
     */
    fun getCode(): Int {
        return mElement.getAnnotation(ToThis::class.java).requestCode
    }




}
