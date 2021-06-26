package org.slimepowered.gradle.tasks.setup.source.provider

interface DecompilerProvider {
    fun decompile(classBytes: ByteArray) : String
}