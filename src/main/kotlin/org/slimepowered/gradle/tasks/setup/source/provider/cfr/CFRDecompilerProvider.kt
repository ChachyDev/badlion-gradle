package org.slimepowered.gradle.tasks.setup.source.provider.cfr

import org.benf.cfr.reader.api.CfrDriver
import org.benf.cfr.reader.api.OutputSinkFactory
import org.slimepowered.gradle.tasks.setup.source.provider.DecompilerProvider
import org.slimepowered.gradle.tasks.setup.source.provider.cfr.sink.CFROutputSink
import java.io.File

private fun createCfrDriver(outputSinkFactory: OutputSinkFactory) =
    CfrDriver.Builder().withOutputSink(outputSinkFactory).build()

class CFRDecompilerProvider : DecompilerProvider {
    override fun decompile(classBytes: ByteArray): String {
        val sink = CFROutputSink()

        val clazz = File.createTempFile("badlion-cfr-output", ".class").apply {
            writeBytes(classBytes)
            deleteOnExit()
        }

        createCfrDriver(sink).analyse(listOf(clazz.absolutePath))

        return sink.decompiled ?: error("Failed to decompile class!")
    }
}