package org.slimepowered.gradle.utils.coroutine

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gradle.api.Task

fun launchCoroutine(name: String, block: suspend CoroutineScope.() -> Unit) =
    CoroutineScope(Dispatchers.IO + CoroutineName(name)).launch(block = block)

fun Task.launchCoroutine(block: suspend CoroutineScope.() -> Unit) =
    CoroutineScope(Dispatchers.IO + CoroutineName("$name Coroutine")).launch(block = block)