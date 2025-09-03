package com.chanzxxx.util.ktbuilder.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate

internal class BuilderProcessor(private val codeGenerator: CodeGenerator): SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(KtBuilder::class.qualifiedName!!)
        val result = symbols.filter { !it.validate() }

        symbols.forEach { symbol ->
            if (symbol is KSClassDeclaration) {
                generateBuilderClass(symbol)
            }
        }
        return result.toList()
    }

    private fun generateBuilderClass(classDeclaration: KSClassDeclaration) {
        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.simpleName.asString()
        val builderClassName = "${className}Builder"

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

            writer.write("    fun build(): $className {\n")
            writer.write("        return $className(\n")
            classDeclaration.getAllProperties().forEach { property ->
                val propName = property.simpleName.asString()
                writer.write("            $propName!!,\n")
            }
            writer.write("        )\n")
            writer.write("    }\n")
            writer.write("}\n")
        }

        val extensionFile = codeGenerator.createNewFile(Dependencies(false), packageName, "${builderClassName}Extensions")

        extensionFile.bufferedWriter().use { writer ->
            writer.write("package $packageName\n")
            writer.write("import kotlin.reflect.KClass\n")
            writer.write("fun ktBuilderFor(clazz: KClass<$className>) = $builderClassName()\n")
        }
    }
}
