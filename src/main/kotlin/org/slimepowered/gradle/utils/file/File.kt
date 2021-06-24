package org.slimepowered.gradle.utils.file

import java.io.File
import java.util.zip.ZipFile

fun File.unzip(extracted: File = File(nameWithoutExtension)): File {
    if (extracted.exists()) return extracted

    extracted.parentFile.mkdirs()
    extracted.mkdir()

    ZipFile(this).use {
        it.entries().asIterator().forEach { entry ->
            val file = File(extracted, entry.name)
            file.parentFile.mkdirs()

            if (entry.isDirectory) {
                file.mkdir()
            } else {
                file.createNewFile()
                file.writeBytes(it.getInputStream(entry).use { `is` -> `is`.readBytes() })
            }
        }
    }

    delete()
    return extracted
}