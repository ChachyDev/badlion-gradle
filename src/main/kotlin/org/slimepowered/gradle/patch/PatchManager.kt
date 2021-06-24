package org.slimepowered.gradle.patch

import com.nothome.delta.GDiffPatcher
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

object PatchManager {
    private val gdiffPatcher = GDiffPatcher()

    fun patch(jarIn: ZipFile, jarOut: File, patches: ZipFile): File {
        jarOut.createNewFile()

        ZipOutputStream(FileOutputStream(jarOut)).use { zos ->
            patches.entries().toList().forEach {
                if (it.name.endsWith(".gdiff")) {
                    val patch = patches.getInputStream(it)
                    val jarEntry = jarIn.getEntry(it.name.removeSuffix(".gdiff"))

                    val patched = if (jarEntry != null) {
                        val origin = jarIn.getInputStream(jarEntry)
                        patch0(origin, patch)
                    } else {
                        patch.use { p -> p.readBytes() }
                    }

                    val newEntry = ZipEntry(it.name.removeSuffix(".gdiff"))

                    zos.putNextEntry(newEntry)
                    zos.write(patched, 0, patched.size)
                    zos.closeEntry()
                } else {
                    val entry = patches.getInputStream(it).use { p -> p.readBytes() }

                    val newEntry = ZipEntry(it)

                    zos.putNextEntry(newEntry)
                    zos.write(entry, 0, entry.size)
                    zos.closeEntry()
                }
            }
        }

        return jarOut
    }

    private fun patch0(source: InputStream, patch: InputStream): ByteArray {
        return patch0(source.use { it.readBytes() }, patch.use { it.readBytes() })
    }

    private fun patch0(source: ByteArray, patch: ByteArray): ByteArray {
        return gdiffPatcher.patch(source, patch)
    }
}