package org.slimepowered.gradle.tasks.setup

import org.gradle.api.tasks.TaskAction
import org.slimepowered.gradle.tasks.BadlionTask

open class SetupTask : BadlionTask() {
    @TaskAction
    fun run() {
        // Just exists
    }
}