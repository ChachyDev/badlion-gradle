package org.slimepowered.gradle.tasks.setup.patch

import org.gradle.api.tasks.TaskAction
import org.slimepowered.gradle.patch.PatchManager
import org.slimepowered.gradle.tasks.BadlionTask
import java.io.File
import java.util.zip.ZipFile

open class PatchMinecraftTask : BadlionTask() {
    @TaskAction
    fun run() {
        val gameVersion = ext.minecraft.get()

        patchedJar = PatchManager.patch(
            ZipFile(cleanJar ?: error("Could not patch the game as the clean jar is missing!")),
            File(gameJarsDir, "${gameVersion}-patched.jar"),
            ZipFile(badlionPatches)
        )
    }
}