package com.chanzxxx.util.ktbuilder.processor

class ExtensionFunctionDeclaration(val className: String, val builderClassName: String)

class ProcessorContext {
    val extensions: MutableList<ExtensionFunctionDeclaration> = mutableListOf()

    fun addExtension(className: String, builderClassName: String) {
        extensions.add(ExtensionFunctionDeclaration(className, builderClassName))
    }
}