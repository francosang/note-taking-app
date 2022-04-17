package com.task.noteapp.ui.fragment.notes

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import coil.compose.AsyncImage
import com.task.noteapp.features.note_list.R
import com.task.noteapp.parcelable.NoteParcelable
import com.task.noteapp.ui.collectAsStateLifecycleAware
import com.task.noteapp.ui.theme.NoteAppTheme
import java.time.LocalDateTime

val CardCorner = RoundedCornerShape(CornerSize(10.dp))

@Composable
fun NotesScreen(
    viewModel: NotesViewModel = hiltViewModel(),
    onNoteTapped: (noteId: Int) -> Unit,
    onAddNote: () -> Unit
) {
    val state by viewModel.state.collectAsStateLifecycleAware()
    when {
        state.isLoading -> {
            Log.i("APP", "Loading")
            FullScreenLoading()
        }
        state.isError -> {
            Log.i("APP", "Error")
            NotesError {
                viewModel.retry()
            }
        }
        else -> {
            Log.i("APP", "Success")
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
    notes: List<NoteParcelable>,
    onNoteTapped: (noteId: Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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
    note: NoteParcelable,
    onNoteTapped: (noteId: Int) -> Unit,
) {
    val title = note.title
    Box(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .clip(CardCorner)
            .border(1.dp, Color.Gray, CardCorner)
    ) {
        Column(
            modifier = Modifier.clickable {
                onNoteTapped(note.id)
            },
        ) {
            if (title != null) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp),
                    text = title,
                    fontSize = 18.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.ExtraBold,
                )
            }

            if (note.image != null && title != null) {
                Box(modifier = Modifier.height(20.dp))
            }

            if (note.image != null) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth(),
                    model = note.image,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp, top = 20.dp),
                text = note.content,
                fontSize = 16.sp,
                maxLines = 2,
                fontWeight = FontWeight.Normal,
            )
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
                NoteParcelable(
                    id = 1,
                    title = "Title and text",
                    content = "Lorem Ipsum lol lol lol Lorem Ipsum lol lol lol Lorem Ipsum lol lol lol Lorem Ipsum lol lol lol",
                    created = LocalDateTime.now(),
                ),
                NoteParcelable(
                    id = 2,
                    content = "Only content",
                    created = LocalDateTime.now(),
                ),
                NoteParcelable(
                    id = 3,
                    image = "https://via.placeholder.com/728x900.png",
                    content = "No title and image",
                    created = LocalDateTime.now(),
                ),
                NoteParcelable(
                    id = 4,
                    title = "Title content and image",
                    image = "https://via.placeholder.com/728x90.png",
                    content = "Lorem Ipsum 2",
                    created = LocalDateTime.now(),
                ),
                NoteParcelable(
                    id = 5,
                    title = "Title content and image",
                    image = "https://via.placeholder.com/90x90.png",
                    content = "Lorem Ipsum 2",
                    created = LocalDateTime.now(),
                ),
            ),
        ),
        onAddNote = {},
        onNoteTapped = {},
    )
}

