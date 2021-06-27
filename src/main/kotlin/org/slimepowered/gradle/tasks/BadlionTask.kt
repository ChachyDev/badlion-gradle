package org.slimepowered.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.slimepowered.gradle.extension.BadlionExtension
import java.io.File

open class BadlionTask : DefaultTask() {
    @Internal
    val ext: BadlionExtension = project.extensions.getByType(BadlionExtension::class.java)

    @Internal
    val cachesDir = File(project.gradle.gradleUserHomeDir, "caches/badlion/${ext.minecraft.get()}/${ext.badlion.get()}")

    @Internal
    val gameJarsDir = File(cachesDir, "game-jars")

    @Internal
    val mappingsDir = File(gameJarsDir, "mappings")

    @Internal
    val patchesDir = File(gameJarsDir, "patches")

    companion object {
        var cleanJar: File? = null

        lateinit var patchedJar: File

        lateinit var badlionPatches: File

        lateinit var badlionMappings: File

        var seargeMappings: File? = null

        var mcpMappings: File? = null

        var finalJar: File? = null

        var gameSources: File? = null

        var assetsDir: File? = null

        var assetsZipped: File? = null
    }
}