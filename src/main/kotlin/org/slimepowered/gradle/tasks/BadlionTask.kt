package org.slimepowered.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.slimepowered.gradle.extension.BadlionExtension
import java.io.File

open class BadlionTask : DefaultTask() {
    @Internal
    val cachesDir = File(project.gradle.gradleUserHomeDir, "caches/badlion")

    @Internal
    val gameJarsDir = File(cachesDir, "game-jars")

    @Internal
    val mappingsDir = File(gameJarsDir, "mappings")

    @Internal
    val patchesDir = File(gameJarsDir, "patches")

    @Internal
    val ext: BadlionExtension = project.extensions.getByType(BadlionExtension::class.java)

    companion object {
        var cleanJar: File? = null

        lateinit var patchedJar: File

        lateinit var badlionPatches: File

        lateinit var badlionMappings: File

        var seargeMappings: File? = null

        var mcpMappings: File? = null

        var finalJar: File? = null

        var gameSources: File? = null
    }
}