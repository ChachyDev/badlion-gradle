package org.slimepowered.gradle.tasks.setup.source.provider.procyon

import com.strobel.decompiler.Decompiler
import com.strobel.decompiler.PlainTextOutput
import org.slimepowered.gradle.tasks.setup.source.provider.DecompilerProvider
import java.io.File

class ProcyonDecompilerProvider : DecompilerProvider {
    override fun decompile(classBytes: ByteArray): String {
        val output = PlainTextOutput()

        val tempFile = File.createTempFile("badlion-Procyon-Decompilation", ".class").apply {
            deleteOnExit()
        }

        classBytes.inputStream().use {
            tempFile.outputStream().use { fos ->
                it.copyTo(fos)
            }
        }

        Decompiler.decompile(tempFile.absolutePath, output)

        return output.toString()
    }
}