package com.task.noteapp.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

// This ugly extension is used to prevent the flow from emitting
// new events when the app is in the background
@Suppress("StateFlowValueCalledInComposition")
@Composable
fun <T> StateFlow<T>.collectAsStateLifecycleAware(
    context: CoroutineContext = EmptyCoroutineContext,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
): State<T> {
    val lifecycleAwareFlow = remember(key1 = this, key2 = lifecycleOwner) {
        this.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    return lifecycleAwareFlow.collectAsState(
        initial = value,
        context = context
    )
}