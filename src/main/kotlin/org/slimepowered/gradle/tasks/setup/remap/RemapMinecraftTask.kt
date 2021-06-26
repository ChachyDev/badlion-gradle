package org.slimepowered.gradle.tasks.setup.remap

import cuchaz.enigma.Enigma
import cuchaz.enigma.ProgressListener
import cuchaz.enigma.translation.mapping.serde.MappingFileNameFormat
import cuchaz.enigma.translation.mapping.serde.MappingFormat
import cuchaz.enigma.translation.mapping.serde.MappingSaveParameters
import net.md_5.specialsource.Jar
import net.md_5.specialsource.JarMapping
import net.md_5.specialsource.JarRemapper
import net.md_5.specialsource.provider.ClassLoaderProvider
import net.md_5.specialsource.provider.JarProvider
import net.md_5.specialsource.provider.JointProvider
import org.gradle.api.tasks.TaskAction
import org.slimepowered.gradle.tasks.BadlionTask
import java.io.File
import java.net.URL
import java.net.URLClassLoader

open class RemapMinecraftTask : BadlionTask() {
    private val enigma: Enigma = Enigma.create()

    @TaskAction
    fun run() {
        val jar = File(gameJarsDir, "${ext.minecraft.get()}-patched.jar")

        val badlionMappingsFolder = badlionMappings.listFiles()?.get(0) ?: return

        val project = enigma.openJar(jar.toPath(), null, ProgressListener.none())

        project.setMappings(
            MappingFormat.ENIGMA_DIRECTORY.read(
                badlionMappingsFolder.toPath(),
                ProgressListener.none(),
                MappingSaveParameters(MappingFileNameFormat.BY_DEOBF)
            )
        )

        val badlionMapped = File(gameJarsDir, "${ext.minecraft.get()}-remapped-badlion-only.jar")
        project.exportRemappedJar(ProgressListener.none()).write(badlionMapped.toPath(), ProgressListener.none())

        val srgMapped = File(gameJarsDir, "${ext.minecraft.get()}-remapped-srg-and-badlion.jar")

        finalJar = File(gameJarsDir, "minecraft-${ext.minecraft.get()}.jar")

        specialsource(badlionMapped, srgMapped, listOf(seargeMappings!!))
        specialsource(srgMapped, finalJar!!, listOf(mcpMappings!!))
    }

    private fun specialsource(`in`: File, out: File, srgFile: List<File>, classpath: List<URL> = emptyList()) {
        val mapping = JarMapping()
        srgFile.forEach { mapping.loadMappings(it) }

        val remapper = JarRemapper(null, mapping)

        val jar = Jar.init(`in`)

        val provider = JointProvider()
        provider.add(JarProvider(jar))

        if (classpath.isNotEmpty()) {
            provider.add(ClassLoaderProvider(URLClassLoader(classpath.toTypedArray())))
        }

        mapping.setFallbackInheritanceProvider(provider)
        remapper.remapJar(jar, out)
    }
}