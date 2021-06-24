package org.slimepowered.gradle.tasks.setup.download

import org.gradle.api.tasks.TaskAction
import org.slimepowered.gradle.tasks.BadlionTask
import org.slimepowered.gradle.utils.constants.PATCHES_URL
import org.slimepowered.gradle.utils.http.download
import java.io.File
import java.net.URL

open class DownloadBadlionPatchesTask : BadlionTask() {
    @TaskAction
    fun run() {
        val badlionVersion = ext.badlion.get()
        val gameVersion = ext.minecraft.get()
        val environment = ext.environmentType.get()


        badlionPatches = URL(PATCHES_URL.format(badlionVersion, gameVersion.replace(".", "_"), environment))
            .download(File(patchesDir, "badlion-patches.zip"))
    }
}