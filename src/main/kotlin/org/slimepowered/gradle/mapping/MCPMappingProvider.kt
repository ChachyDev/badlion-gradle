package org.slimepowered.gradle.mapping

object MCPMappingProvider {
    private const val MCP_URL = "http://export.mcpbot.bspk.rs/mcp/%s/mcp-%s-srg.zip"

    operator fun get(gameVersion: String): String {
        return MCP_URL.format(gameVersion, gameVersion)
    }
}