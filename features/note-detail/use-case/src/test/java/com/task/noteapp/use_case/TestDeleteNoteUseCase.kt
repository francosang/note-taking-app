package com.task.noteapp.use_case

import com.task.noteapp.commons.logger.Logger
import com.task.store.specification.NoteStore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class TestDeleteNoteUseCase {

    private val dispatcher = TestCoroutineDispatcher()

    private val logger = mockk<Logger>()
    private val noteStore = mockk<NoteStore>()

    @Test
    fun `DeleteNote should return success result when store does not fail`() {
        coEvery { noteStore.deleteNote(1) } just runs

        val useCase = DeleteNoteUseCase(
            dispatcher = dispatcher,
            logger = logger,
            noteStore = noteStore,
        )

        val result = runBlocking {
            useCase(1)
        }

        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), Unit)
        assertNull(result.exceptionOrNull())

        coVerify(exactly = 1) { noteStore.deleteNote(1) }
        coVerify(exactly = 0) { logger.e(t = any<Exception>()) }
    }

    @Test
    fun `DeleteNote should return error result when store fails`() {
        coEvery { noteStore.deleteNote(1) } throws Exception("I am failing")
        coEvery { logger.e(t = any<Exception>()) } just runs

        val useCase = DeleteNoteUseCase(
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

        coVerify(exactly = 1) { noteStore.deleteNote(1) }
        coVerify(exactly = 1) { logger.e(t = any<Exception>()) }
    }
}
