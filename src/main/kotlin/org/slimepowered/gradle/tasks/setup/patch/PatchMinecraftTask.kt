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

        val jar = cleanJar ?: error("Could not patch the game as the clean jar is missing!")

        if (!jar.exists()) error("Could not patch the game! The clean jar didn't get downloaded? Please report this to the maintainers of badlion-gradle...")

        patchedJar = PatchManager.patch(
            ZipFile(jar),
            File(gameJarsDir, "${gameVersion}-patched.jar"),
            ZipFile(badlionPatches)
        )
    }
}