package com.task.noteapp.ui.fragment.notes

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.task.noteapp.domain.Note
import com.task.noteapp.features.note_list.R
import com.task.noteapp.ui.collectAsStateLifecycleAware
import com.task.noteapp.ui.component.DateLabel
import com.task.noteapp.ui.component.NoteImage
import com.task.noteapp.ui.theme.CardCorner
import com.task.noteapp.ui.theme.NoteAppTheme
import java.time.LocalDateTime

@Composable
fun NotesScreen(
    viewModel: NotesViewModel = hiltViewModel(),
    onNoteTapped: (noteId: Int) -> Unit,
    onAddNote: () -> Unit
) {
    val state by viewModel.state.collectAsStateLifecycleAware()
    when {
        state.isLoading -> {
            FullScreenLoading()
        }
        state.isError -> {
            NotesError {
                viewModel.retry()
            }
        }
        else -> {
            NotesLoaded(
                state = state,
                onNoteTapped = onNoteTapped,
                onAddNote = onAddNote,
            )
        }
    }
}

@Composable
fun NotesLoaded(
    state: NotesScreenState,
    onNoteTapped: (noteId: Int) -> Unit,
    onAddNote: () -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Icon(Icons.Filled.Add, contentDescription = "Add new note")
            }
        }
    ) {
        if (state.notes.isNotEmpty()) {
            NotesList(
                notes = state.notes,
                onNoteTapped = onNoteTapped,
            )
        } else {
            NoteEmpty()
        }
    }
}

@Composable
fun NotesList(
    notes: List<Note>,
    onNoteTapped: (noteId: Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxSize(),
    ) {
        items(notes) { item ->
            NoteItem(
                note = item,
                onNoteTapped = onNoteTapped,
            )
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onNoteTapped: (noteId: Int) -> Unit,
) {
    val title = note.title
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
            .fillMaxWidth()
            .clip(CardCorner)
            .border(1.dp, Color.Gray, CardCorner)
    ) {
        DateLabel(
            modifier = Modifier
                .padding(6.dp)
                .align(Alignment.TopEnd),
            edited = note.edited,
            created = note.created,
            fontSize = 10.sp,
        )

        Column(
            modifier = Modifier.clickable {
                note.id?.let {
                    onNoteTapped(it)
                }
            },
        ) {
            var noteTopPadding = 20.dp
            if (title != null) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 20.dp),
                    text = title,
                    fontSize = 18.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.ExtraBold,
                )

                noteTopPadding = 0.dp
            }

            if (note.image != null) {
                NoteImage(
                    modifier = Modifier
                        .fillMaxWidth(),
                    localUri = Uri.parse(note.image),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                )

                noteTopPadding = 20.dp
            }

            if (note.content.isNotBlank()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp, top = noteTopPadding),
                    text = note.content,
                    fontSize = 16.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun NotesError(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(R.string.notes_error_message),
                fontSize = 22.sp,
            )
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.message_retry))
            }
        }
    }
}

@Composable
fun NoteEmpty() {
    Text(
        modifier = Modifier
            .padding(40.dp)
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        textAlign = TextAlign.Center,
        text = stringResource(R.string.notes_empty_message),
        fontSize = 32.sp,
        fontWeight = FontWeight.Light
    )
}

@Preview
@Composable
fun NotesListPreview() {
    NoteAppTheme {
        NotesListFilled()
    }
}

@Composable
fun NotesListFilled() {
    NotesLoaded(
        state = NotesScreenState(
            isLoading = false,
            isError = false,
            notes = listOf(
                Note(
                    id = 1,
                    title = "Title and text",
                    content = "Lorem Ipsum lol lol lol Lorem Ipsum lol lol lol Lorem Ipsum lol lol lol Lorem Ipsum lol lol lol",
                    created = LocalDateTime.now(),
                    edited = null,
                    image = null,
                ),
                Note(
                    id = 2,
                    title = null,
                    content = "Only content",
                    created = LocalDateTime.now(),
                    edited = null,
                    image = null,
                ),
                Note(
                    id = 3,
                    title = null,
                    image = "https://via.placeholder.com/728x900.png",
                    content = "No title and image",
                    created = LocalDateTime.now(),
                    edited = null,
                ),
                Note(
                    id = 4,
                    title = "Title content and image",
                    image = "https://via.placeholder.com/728x90.png",
                    content = "Lorem Ipsum 2",
                    created = LocalDateTime.now(),
                    edited = null,
                ),
                Note(
                    id = 5,
                    title = "Title content and image",
                    image = "https://via.placeholder.com/90x90.png",
                    content = "Lorem Ipsum 2",
                    created = LocalDateTime.now(),
                    edited = null,

                ),
            ),
        ),
        onAddNote = {},
        onNoteTapped = {},
    )
}
