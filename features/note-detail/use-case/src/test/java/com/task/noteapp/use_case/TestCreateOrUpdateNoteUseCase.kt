package com.task.noteapp.use_case

import com.task.noteapp.commons.logger.Logger
import com.task.noteapp.commons.test.NoteMocks
import com.task.store.specification.NoteStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Test

@ExperimentalCoroutinesApi
class TestCreateOrUpdateNoteUseCase {

    private val dispatcher = TestCoroutineDispatcher()

    private val logger = mockk<Logger>(relaxed = true)
    private val noteStore = mockk<NoteStore>()

    @Test
    fun `CreateOrUpdateNote should return null when params are null`() {
        val useCase = CreateOrUpdateNoteUseCase(
            dispatcher = dispatcher,
            logger = logger,
            noteStore = noteStore,
        )

        val result = runBlocking {
            useCase(
                CreateOrUpdateNoteUseCase.Params(
                    noteId = null,
                    note = "",
                    title = null,
                    image = null,
                )
            )
        }

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())

        coVerify(exactly = 0) { noteStore.getNote(any()) }
        coVerify(exactly = 0) { noteStore.save(any()) }
        coVerify(exactly = 0) { logger.e(t = any<Exception>()) }
    }

    @Test
    fun `CreateOrUpdateNote should return null when params are empty blank strings`() {
        val useCase = CreateOrUpdateNoteUseCase(
            dispatcher = dispatcher,
            logger = logger,
            noteStore = noteStore,
        )

        val result = runBlocking {
            useCase(
                CreateOrUpdateNoteUseCase.Params(
                    noteId = null,
                    note = "   ",
                    title = "   ",
                    image = "   ",
                )
            )
        }

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())

        coVerify(exactly = 0) { noteStore.getNote(any()) }
        coVerify(exactly = 0) { noteStore.save(any()) }
        coVerify(exactly = 0) { logger.e(t = any<Exception>()) }
    }

    @Test
    fun `CreateOrUpdateNote should save when at least note param is received`() {
        coEvery { noteStore.save(any()) } answers {
            firstArg()
        }
        val useCase = CreateOrUpdateNoteUseCase(
            dispatcher = dispatcher,
            logger = logger,
            noteStore = noteStore,
        )

        val result = runBlocking {
            useCase(
                CreateOrUpdateNoteUseCase.Params(
                    noteId = null,
                    note = "Note",
                    title = null,
                    image = null,
                )
            )
        }

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertNotNull(result.getOrNull()?.id)

        coVerify(exactly = 0) { noteStore.getNote(any()) }
        coVerify(exactly = 1) { noteStore.save(any()) }
        coVerify(exactly = 0) { logger.e(t = any<Exception>()) }
    }

    @Test
    fun `CreateOrUpdateNote should save when at least title param is received`() {
        coEvery { noteStore.save(any()) } answers { firstArg() }
        val useCase = CreateOrUpdateNoteUseCase(
            dispatcher = dispatcher,
            logger = logger,
            noteStore = noteStore,
        )

        val result = runBlocking {
            useCase(
                CreateOrUpdateNoteUseCase.Params(
                    noteId = null,
                    note = "",
                    title = "Title",
                    image = null,
                )
            )
        }

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertNotNull(result.getOrNull()?.id)

        coVerify(exactly = 0) { noteStore.getNote(any()) }
        coVerify(exactly = 1) { noteStore.save(any()) }
        coVerify(exactly = 0) { logger.e(t = any<Exception>()) }
    }

    @Test
    fun `CreateOrUpdateNote should retrieve persisted note if id param is received`() {
        coEvery { noteStore.getNote(1) } returns NoteMocks.persistedWithTitle
        coEvery { noteStore.save(any()) } answers { firstArg() }
        val useCase = CreateOrUpdateNoteUseCase(
            dispatcher = dispatcher,
            logger = logger,
            noteStore = noteStore,
        )

        val result = runBlocking {
            useCase(
                CreateOrUpdateNoteUseCase.Params(
                    noteId = 1,
                    note = "New note",
                    title = null,
                    image = null,
                )
            )
        }

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(result.getOrNull()?.id, 1)
        assertEquals(result.getOrNull()?.title, null)
        assertEquals(result.getOrNull()?.content, "New note")

        coVerify(exactly = 1) { noteStore.getNote(any()) }
        coVerify(exactly = 1) { noteStore.save(any()) }
        coVerify(exactly = 0) { logger.e(t = any<Exception>()) }
    }

    @Test
    fun `CreateOrUpdateNote should return save empty note if it has id`() {
        coEvery { noteStore.getNote(1) } returns NoteMocks.persistedWithTitleAndImage
        coEvery { noteStore.save(any()) } answers { firstArg() }

        val useCase = CreateOrUpdateNoteUseCase(
            dispatcher = dispatcher,
            logger = logger,
            noteStore = noteStore,
        )

        val result = runBlocking {
            useCase(
                CreateOrUpdateNoteUseCase.Params(
                    noteId = 1,
                    note = "", // empty note with id
                    title = null,
                    image = null,
                )
            )
        }

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(result.getOrNull()?.id, 1)
        assertEquals(result.getOrNull()?.title, null)
        assertEquals(result.getOrNull()?.content, "")
        assertEquals(result.getOrNull()?.image, null)

        coVerify(exactly = 1) { noteStore.getNote(any()) }
        coVerify(exactly = 1) { noteStore.save(any()) }
        coVerify(exactly = 0) { logger.e(t = any<Exception>()) }
    }

    @Test
    fun `CreateOrUpdateNote should return error result when store fails`() {
        coEvery { noteStore.deleteNote(1) } throws Exception("I am failing")
        coEvery { logger.e(t = any<Exception>()) } just runs

        val useCase = CreateOrUpdateNoteUseCase(
            dispatcher = dispatcher,
            logger = logger,
            noteStore = noteStore,
        )

        val result = runBlocking {
            useCase(
                CreateOrUpdateNoteUseCase.Params(
                    noteId = 1,
                    note = "New note",
                    title = null,
                    image = null,
                )
            )
        }

        assertTrue(result.isFailure)
        assertNull(result.getOrNull())
        assertNotNull(result.exceptionOrNull())

        coVerify(exactly = 1) { logger.e(t = any<Exception>()) }
    }
}
