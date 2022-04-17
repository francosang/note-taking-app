package com.task.noteapp.lib

import com.task.noteapp.commons.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

abstract class UseCase<in P, R>(
    private val dispatcher: CoroutineDispatcher,
    private val logger: Logger,
) {

    /** Executes the use case asynchronously and returns a [Result].
     *
     * @return a [Result].
     *
     * @param parameters the input parameters to run the use case with
     */
    suspend operator fun invoke(parameters: P): Result<R> {
        return withContext(dispatcher) {
            try {
                execute(parameters).let {
                    Result.success(it)
                }
            } catch (e: Exception) {
                logger.e(e)
                Result.failure(e)
            }
        }
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(params: P): R
}

@ExperimentalCoroutinesApi
abstract class ObservableUseCase<P : Any, T>(
    private val logger: Logger,
) {
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val flow: Flow<Result<T>> = paramState
        .distinctUntilChanged()
        .flatMapLatest { params ->
            createObservable(params).map {
                Result.success(it)
            }
        }
        .catch {
            logger.e(it)
            emit(Result.failure(it))
        }
        .distinctUntilChanged()

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<T>
}
