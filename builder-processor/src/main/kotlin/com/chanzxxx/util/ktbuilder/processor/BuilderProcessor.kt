package com.chanzxxx.util.ktbuilder.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate

internal class BuilderProcessor(private val codeGenerator: CodeGenerator,
                                private val kspLogger: KSPLogger): SymbolProcessor {
    companion object {
        var round = 0
    }
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val context = ProcessorContext()
        val symbols = resolver.getSymbolsWithAnnotation(KtBuilder::class.qualifiedName!!)
        val result = symbols.filter { !it.validate() }

        symbols.forEach { symbol ->
            if (symbol is KSClassDeclaration) {
                generateBuilderClass(context, symbol)
            }
        }

        if (context.extensions.isNotEmpty()) {
            val extensionFile = codeGenerator.createNewFile(Dependencies(false), "com.chanzxxx.util.ktbuilder.processor", "GeneratedExtensions$round")

            extensionFile.bufferedWriter().use { writer ->
                writer.write("package com.chanzxxx.util.ktbuilder.processor\n")
                writer.write("import kotlin.reflect.KClass\n")

                context.extensions.forEach { extension ->
                    writer.write("fun ktBuilderFor(clazz: KClass<${extension.className}>) = ${extension.builderClassName}()\n")
                }
            }
        }

        round++
        return result.toList()
    }

    private fun builderClassName(classDeclaration: KSClassDeclaration): String {
        if (classDeclaration.parentDeclaration == null
            || classDeclaration.parentDeclaration !is KSClassDeclaration) {
            return "${classDeclaration.simpleName.asString()}Builder"
        }

        val parentClass = classDeclaration.parentDeclaration as KSClassDeclaration
        return "${parentClass.simpleName.asString()}${classDeclaration.simpleName.asString()}Builder"
    }

    private fun generateBuilderClass(context: ProcessorContext, classDeclaration: KSClassDeclaration) {
        val packageName = classDeclaration.packageName.asString()
        val builderClassName = builderClassName(classDeclaration)
        val classFullName = classDeclaration.qualifiedName?.asString() ?: return

        val file = codeGenerator.createNewFile(Dependencies(false), packageName, builderClassName)
        file.bufferedWriter().use { writer ->
            writer.write("package $packageName\n\n")
            writer.write("class $builderClassName {\n")

            classDeclaration.getAllProperties().forEach { property ->
                val propName = property.simpleName.asString()
                val propType = property.type.resolve().declaration.simpleName.asString()
                writer.write("    var $propName: $propType? = null\n")
            }

            classDeclaration.getAllProperties().forEach { property ->
                val propName = property.simpleName.asString()
                val propType = property.type.resolve().declaration.simpleName.asString()
                writer.write("    fun $propName(v: $propType): $builderClassName {\n")
                writer.write("       this.$propName = v\n")
                writer.write("       return this\n")
                writer.write("    }\n")
            }

            writer.write("    fun build(): $classFullName {\n")
            classDeclaration.getAllProperties().forEach { property ->
                if (property.type.resolve().isMarkedNullable) {
                    return@forEach
                }

                val propName = property.simpleName.asString()
                writer.write("        requireNotNull(${propName})\n")
            }

            writer.write("        return $classFullName(\n")
            classDeclaration.getAllProperties().forEach { property ->
                val propName = property.simpleName.asString()
                writer.write("            $propName!!,\n")
            }
            writer.write("        )\n")
            writer.write("    }\n")
            writer.write("}\n")
        }

        context.addExtension(classFullName, "$packageName.$builderClassName")
    }
}
