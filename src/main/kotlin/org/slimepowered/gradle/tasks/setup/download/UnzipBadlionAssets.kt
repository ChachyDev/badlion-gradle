package org.slimepowered.gradle.tasks.setup.download

import org.gradle.api.tasks.TaskAction
import org.slimepowered.gradle.BadlionGradlePlugin
import org.slimepowered.gradle.tasks.BadlionTask
import org.slimepowered.gradle.utils.file.unzip
import java.io.File

open class UnzipBadlionAssets : BadlionTask() {
    @TaskAction
    fun run() {
        val assetsZip = assetsZipped!!
        val unzipped = File(assetsDir, "objects")
        if (unzipped.listFiles()?.size ?: 0 < 0) {
            error("Please rerun the setup task, as set extraction has failed meaning it was most likely not downloaded correctly.")
        }
        assetsZip.unzip(unzipped)

        BadlionGradlePlugin.addLibrary(unzipped)
    }
}