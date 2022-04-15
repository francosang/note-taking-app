@file:Suppress("unused")

package com.task.noteapp.lib

import com.task.noteapp.commons.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
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
