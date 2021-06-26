package org.slimepowered.gradle.tasks.setup.download

import com.google.gson.JsonObject
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.gradle.api.tasks.TaskAction
import org.slimepowered.gradle.BadlionGradlePlugin
import org.slimepowered.gradle.tasks.BadlionTask
import org.slimepowered.gradle.tasks.setup.download.data.MinecraftVersionManifests
import org.slimepowered.gradle.tasks.setup.download.schema.legacy.LegacySchema
import org.slimepowered.gradle.utils.constants.VERSION_MANIFEST
import org.slimepowered.gradle.utils.http.download
import org.slimepowered.gradle.utils.http.http
import java.io.File

open class DownloadMinecraftTask : BadlionTask() {
    @TaskAction
    fun run() {
        minecraft(ext.minecraft.get())
    }

    private fun minecraft(gameVersion: String) {
        val manifestUrl =
            runBlocking { http.get<MinecraftVersionManifests>(VERSION_MANIFEST).versions.find { it.id == gameVersion }?.url }
                ?: error("Failed to find Minecraft $gameVersion, is it actually a Minecraft version?")

        runBlocking {
            http.get<JsonObject>(manifestUrl).let {
                val schemaVer = it.getAsJsonPrimitive("minimumLauncherVersion").asInt

                val schema = when {
                    schemaVer >= 14 -> LegacySchema
                    else -> error("Could not find schema handler for $gameVersion. (MinimumLauncherVersion: $schemaVer)")
                }

                if (cleanJar == null) {
                    cleanJar = schema.client(it).download(File(gameJarsDir, "$gameVersion-clean.jar"))
                }

                schema.libraries(it).forEach { lib ->
                    BadlionGradlePlugin.addLibrary(lib)
                }
            }
        }
    }
}