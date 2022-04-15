package com.task.noteapp.use_case

import com.task.noteapp.commons.logger.Logger
import com.task.store.specification.NoteStore
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TestGetAllNotesUseCase {

    private val dispatcher = TestCoroutineDispatcher()

    private val logger = mockk<Logger>()
    private val noteStore = mockk<NoteStore>()

    private val useCase = GetAllNotesUseCase(
        dispatcher = dispatcher,
        logger = logger,
        noteStore = noteStore,
    )

    @Before
    fun before() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return success result when store does not fail`() {
        coEvery { noteStore.getNotes() } returns emptyList()

        val result = runBlocking {
            useCase(Unit)
        }

        assertEquals(result.isSuccess, true)
        assertEquals(result.getOrNull() != null, true)
    }

    @Test
    fun `should return error result when store fails`() {
        coEvery { noteStore.getNotes() } throws Exception("I am failing")
        coEvery { logger.e(t = any()) } returns Unit

        val result = runBlocking {
            useCase(Unit)
        }

        assertEquals(result.isFailure, true)
        assertEquals(result.getOrNull() == null, true)
    }
}
