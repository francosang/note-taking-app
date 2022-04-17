package com.task.noteapp.use_case

import com.task.noteapp.commons.logger.Logger
import com.task.store.specification.NoteStore
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class TestGetAllNotesUseCase {

    private val logger = mockk<Logger>()
    private val noteStore = mockk<NoteStore>()

    private val useCase = GetAllNotesUseCase(
        logger = logger,
        noteStore = noteStore,
    )

    @Test
    fun `should return success result when store does not fail`() {
        coEvery { noteStore.observeNotes() } returns flow {
            emit(emptyList())
        }

        val result = runBlocking {
            useCase(Unit)

            useCase.flow.first()
        }

        assertEquals(result.isSuccess, true)
        assertEquals(result.getOrNull() != null, true)
    }

    @Test
    fun `should return error result when store fails`() {
        coEvery { noteStore.observeNotes() } throws Exception("I am failing")
        coEvery { logger.e(t = any()) } returns Unit

        val result = runBlocking {
            useCase(Unit)

            useCase.flow.first()
        }

        assertEquals(result.isFailure, true)
        assertEquals(result.getOrNull() == null, true)
    }
}
