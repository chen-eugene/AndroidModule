package com.eugene.aop.router

interface Resolver<T> {

    fun resolve(host: T)

}
