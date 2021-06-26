package org.slimepowered.gradle.tasks.setup.source

import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.tasks.TaskAction
import org.slimepowered.gradle.BadlionGradlePlugin
import org.slimepowered.gradle.tasks.BadlionTask
import org.slimepowered.gradle.tasks.setup.source.provider.DecompilerProvider
import java.io.File
import java.nio.file.Files
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream


open class GenerateSourceJar : BadlionTask() {
    @TaskAction
    fun run() {
        val md = MessageDigest.getInstance("MD5")

        val src = File(gameJarsDir, finalJar!!.nameWithoutExtension + "-sources.jar").apply {
            parentFile?.mkdirs()
            createNewFile()
        }
        gameSources = src

        ZipOutputStream(src.outputStream()).use { zos ->
            ZipFile(finalJar!!).use { file ->
                file.entries().iterator().forEach {
                    if (it.name.endsWith(".class")) {
                        val bytes = decompile(
                            file.getInputStream(it).use { `is` -> `is`.readBytes() },
                            ext.decompiler.get().decompiler
                        ).toByteArray()

                        zos.putNextEntry(ZipEntry(it.name.replace(".class", ".java")))
                        zos.write(bytes, 0, bytes.size)
                        zos.closeEntry()
                    } else {
                        zos.putNextEntry(it)
                        val bytes = file.getInputStream(it).use { `is` -> `is`.readBytes() }
                        zos.write(bytes, 0, bytes.size)
                        zos.closeEntry()
                    }
                }
            }
        }

        File(src.parentFile, finalJar!!.name + ".md5").apply {
            md.update(finalJar!!.readBytes())

            writeText(hash(finalJar!!))
        }

        File(src.parentFile, src.name + ".md5").apply {
            md.update(src.readBytes())

            createNewFile()
            writeText(hash(src))
        }

        BadlionGradlePlugin.addLibrary("net.minecraft:minecraft:${ext.minecraft.get()}")
        BadlionGradlePlugin.addLibrary(src)
    }

    private fun decompile(bytes: ByteArray, decompilerProvider: DecompilerProvider): String {
        return decompilerProvider.decompile(bytes)
    }

    fun hash(file: File): String {
        file.inputStream().use {
            return DigestUtils.md5Hex(it)
        }
    }
}