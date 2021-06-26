package org.slimepowered.gradle

import io.ktor.util.collections.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slimepowered.gradle.tasks.BadlionTask.Companion.finalJar
import org.slimepowered.gradle.tasks.setup.SetupTask
import org.slimepowered.gradle.tasks.setup.source.GenerateSourceJar
import org.slimepowered.gradle.tasks.setup.download.DownloadBadlionPatchesTask
import org.slimepowered.gradle.tasks.setup.download.DownloadMinecraftTask
import org.slimepowered.gradle.tasks.setup.patch.PatchMinecraftTask
import org.slimepowered.gradle.tasks.setup.remap.DownloadMappingsTask
import org.slimepowered.gradle.tasks.setup.remap.RemapMinecraftTask
import org.slimepowered.gradle.utils.constants.TASK_GROUP
import org.slimepowered.gradle.utils.constants.TASK_OTHER
import org.slimepowered.gradle.extension.BadlionExtension
import java.io.File

class BadlionGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("badlion", BadlionExtension::class.java)

        project.configurations.register("badlion").configure {
            project.configurations.getByName("implementation").extendsFrom(it)
        }

        project.repositories.apply {
            mavenCentral()
            maven {
                it.setUrl("https://libraries.minecraft.net")
            }
            flatDir {
                it.dir(File(project.gradle.gradleUserHomeDir, "caches/badlion/game-jars"))
            }
        }

        project.afterEvaluate {
            afterEvaluate(it)
        }
    }

    private fun afterEvaluate(project: Project) {
        File(
            project.gradle.gradleUserHomeDir.absolutePath + "/game-jars",
            "${project.extensions.getByType(BadlionExtension::class.java).minecraft.get()}-remapped-revision-1.jar"
        ).let {
            if (it.exists()) {
                finalJar = it
            }
        }

        finalJar?.let { librariesToBeAdded.add(it) }

        for (lib in librariesToBeAdded) {
            project.dependencies.add("badlion", if (lib is File) project.files(lib) else lib)
        }

        project.tasks.apply {
            val setup = register("setup", SetupTask::class.java).get()
            val downloadMinecraft = register("downloadMinecraft", DownloadMinecraftTask::class.java).get()
            val downloadBadlionPatches =
                register("downloadBadlionPatches", DownloadBadlionPatchesTask::class.java).get()
            val patchMinecraft = register("patchMinecraft", PatchMinecraftTask::class.java).get()
            val downloadMappings = register("downloadMappings", DownloadMappingsTask::class.java).get()
            val remapMinecraft = register("remapMinecraft", RemapMinecraftTask::class.java).get()
            val decompileGameTask = register("decompileGameTask", GenerateSourceJar::class.java).get()

            setup.group = TASK_GROUP
            downloadMinecraft.group = TASK_OTHER
            downloadBadlionPatches.group = TASK_OTHER

            setup.dependsOn(decompileGameTask) // Depend on the last task to be ran and chain down them
            decompileGameTask.dependsOn(remapMinecraft)
            remapMinecraft.dependsOn(downloadMappings)
            downloadMappings.dependsOn(patchMinecraft)
            patchMinecraft.dependsOn(downloadBadlionPatches)
            downloadBadlionPatches.dependsOn(downloadMinecraft)

            downloadMinecraft.run()
        }
    }

    companion object {
        val librariesToBeAdded = ConcurrentList<Any>()

        fun addLibrary(file: Any) {
            librariesToBeAdded.add(file)
        }
    }
}