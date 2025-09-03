package com.chazxxx.util.ktbuilder

import com.chanzxxx.util.ktbuilder.processor.KtBuilder

@KtBuilder
class Company(
    val name: String,
    val money: Money? = null
) {
    @KtBuilder
    class InnerClass(val prop1: String, val prop2: String)
}