package org.slimepowered.gradle.tasks.setup.download

import org.gradle.api.tasks.TaskAction
import org.slimepowered.gradle.tasks.BadlionTask
import org.slimepowered.gradle.utils.constants.ASSETS_INDEX
import org.slimepowered.gradle.utils.constants.ASSETS_URL
import org.slimepowered.gradle.utils.http.download
import java.io.File
import java.net.URL

open class DownloadBadlionAssets : BadlionTask() {
    @TaskAction
    fun run() {
        val environment = ext.environmentType.get()
        val badlionVersion = ext.badlion.get()

        val dir = File(cachesDir, "game-assets")
        dir.mkdirs()

        assetsDir = dir

        URL(ASSETS_INDEX.format(environment, badlionVersion)).download(File(dir, "assets.json"))

        assetsZipped =
            URL(ASSETS_URL.format(environment, badlionVersion)).download(File(dir, "assets.zip"))
    }
}