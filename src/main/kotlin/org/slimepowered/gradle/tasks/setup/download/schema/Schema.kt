package org.slimepowered.gradle.tasks.setup.download.schema

import com.google.gson.JsonObject
import java.net.URL

interface Schema {
    /**
     * Returns the location, on the internet, to the client jar of the game.
     *
     * @author ChachyDev
     * @since 1.0.0
     */
    fun client(manifest: JsonObject): URL

    fun libraries(manifest: JsonObject): List<String>
}