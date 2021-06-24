package org.slimepowered.gradle.utils.http

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*

val http = HttpClient(Apache) {
    Json {
        serializer = GsonSerializer()
    }
}