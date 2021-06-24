package org.slimepowered.gradle.tasks.setup.remap

import kotlinx.coroutines.runBlocking
import org.gradle.api.tasks.TaskAction
import org.slimepowered.gradle.mapping.SrgToMCPProvider
import org.slimepowered.gradle.mapping.MCPMappingProvider
import org.slimepowered.gradle.tasks.BadlionTask
import org.slimepowered.gradle.utils.file.unzip
import org.slimepowered.gradle.utils.http.download
import org.slimepowered.gradle.utils.http.latest
import java.io.File
import java.net.URL

open class DownloadMappingsTask : BadlionTask() {
    @TaskAction
    fun run() {
        runBlocking {
            badlionMappings = URL(latest("SlimePowered/badlion-mappings"))
                .download(File(mappingsDir, "badlion-mappings.zip"))
                .unzip(File(mappingsDir, "badlion-extracted"))

            seargeMappings = URL(MCPMappingProvider[ext.minecraft.get()])
                .download(File(mappingsDir, "mcp.zip"))
                .unzip(File(mappingsDir, "mcp"))
                .listFiles()
                ?.find { it.name == "joined.srg" }
                ?: error("Failed to find joined.srg in mcp-extracted")

            mcpMappings = SrgToMCPProvider.generate(ext.minecraft.get(), File(mappingsDir, "srg-merged/srg-mcp.srg"))
        }
    }
}