package com.cathaybk.travel

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

fun <T> StateFlow<T>.waitUntilChanged(timeoutMs: Long = 500): T {
    val oldValue = this.value
    return runBlocking {
        withTimeout(timeoutMs) {
            first { oldValue != it }
        }
    }
}