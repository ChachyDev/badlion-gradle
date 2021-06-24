package org.slimepowered.gradle.tasks.setup.download.data

data class MinecraftVersionManifest(val downloads: Downloads) {
    data class Downloads(val client: Client) {
        data class Client(val url: String)
    }
}