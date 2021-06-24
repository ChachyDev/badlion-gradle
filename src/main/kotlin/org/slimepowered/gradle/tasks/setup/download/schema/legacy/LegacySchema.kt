package org.slimepowered.gradle.tasks.setup.download.schema.legacy

import com.google.gson.JsonObject
import org.slimepowered.gradle.tasks.setup.download.schema.Schema
import java.net.URL

object LegacySchema : Schema {
    override fun client(manifest: JsonObject): URL {
        return URL(
            manifest.getAsJsonObject("downloads")
                .getAsJsonObject("client")
                .getAsJsonPrimitive("url")
                .asString
        )
    }

    override fun libraries(manifest: JsonObject): List<String> {
        return manifest.get("libraries").asJsonArray
            .mapNotNull { it.asJsonObject.getAsJsonPrimitive("name").asString.takeIf { t -> !t.contains("twitch") } }
            .toMutableList()
            .apply {
                add("net.minecraft:launchwrapper:1.12")
            }
    }
}