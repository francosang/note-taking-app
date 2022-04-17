package com.task.noteapp.use_case

import com.task.noteapp.commons.logger.Logger
import com.task.noteapp.commons.test.NoteMocks
import com.task.store.specification.NoteStore
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class TestGetNoteUseCase {

    private val dispatcher = TestCoroutineDispatcher()

    private val logger = mockk<Logger>()
    private val noteStore = mockk<NoteStore>()

    @Test
    fun `GetNote should return success result when store does not fail`() {
        coEvery { noteStore.getNote(1) } returns NoteMocks.empty

        val useCase = GetNoteUseCase(
            dispatcher = dispatcher,
            logger = logger,
            noteStore = noteStore,
        )

        val result = runBlocking {
            useCase(1)
        }

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertNull(result.exceptionOrNull())

        coVerify(exactly = 1) { noteStore.getNote(1) }
        coVerify(exactly = 0) { logger.e(t = any<Exception>()) }
    }

    @Test
    fun `GetNote should return error result when store fails`() {
        coEvery { noteStore.getNote(1) } throws Exception("I am failing")
        coEvery { logger.e(t = any<Exception>()) } just runs

        val useCase = GetNoteUseCase(
            dispatcher = dispatcher,
            logger = logger,
            noteStore = noteStore,
        )

        val result = runBlocking {
            useCase(1)
        }

        assertTrue(result.isFailure)
        assertNull(result.getOrNull())
        assertNotNull(result.exceptionOrNull())

        coVerify(exactly = 1) { noteStore.getNote(1) }
        coVerify(exactly = 1) { logger.e(t = any<Exception>()) }
    }
}
