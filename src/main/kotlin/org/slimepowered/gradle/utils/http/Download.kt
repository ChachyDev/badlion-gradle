package org.slimepowered.gradle.utils.http

import com.google.gson.annotations.SerializedName
import io.ktor.client.request.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

private data class LatestGithub(@SerializedName("zipball_url") val zip: String)

/**
 * Get latest asset from a GitHub repository.
 */
suspend fun latest(repo: String): String {
    return http
        .get<List<LatestGithub>>("https://api.github.com/repos/$repo/tags")[0]
        .zip
}

fun URL.download(dest: File): File {
    if (dest.exists()) return dest // Download if needed :)

    dest.parentFile.mkdirs()
    dest.createNewFile()

    BufferedInputStream(openStream()).use { `in` ->
        FileOutputStream(dest).use { fos ->
            `in`.copyTo(fos)
        }
    }

    return dest
}