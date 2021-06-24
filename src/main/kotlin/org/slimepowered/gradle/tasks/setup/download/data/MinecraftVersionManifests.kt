package org.slimepowered.gradle.tasks.setup.download.data

data class MinecraftVersionManifests(val versions: List<Manifest>) {
    data class Manifest(val id: String, val url: String)
}