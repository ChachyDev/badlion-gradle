package org.slimepowered.gradle.mapping

import dev.dreamhopping.coordinate.Coordinate
import dev.dreamhopping.coordinate.provider.MappingProviderType
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File

object SrgToMCPProvider {
    @OptIn(ExperimentalStdlibApi::class)
    fun generate(version: String, out: File): File {
        if (out.exists()) return out
        out.parentFile?.mkdirs()
        out.createNewFile()

        Coordinate.getMappings(version, MappingProviderType.MCP).let {
            val file = buildList {
                it.fields.forEach {
                    add("FD: ${it.deobfuscatedOwner}/${it.obfuscatedName} ${it.deobfuscatedOwner}/${it.deobfuscatedName}")
                }

                it.methods.forEach {
                    add("MD: ${it.deobfuscatedOwner}/${it.obfuscatedName} ${it.deobfuscatedDescriptor} ${it.deobfuscatedOwner}/${it.deobfuscatedName} ${it.deobfuscatedDescriptor}")
                }
            }.joinToString("\n")

            out.writeText(file)
        }

        return out
    }
}