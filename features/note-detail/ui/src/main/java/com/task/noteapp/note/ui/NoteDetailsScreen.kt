package com.task.noteapp.note.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.task.noteapp.ui.theme.NoteAppTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val Formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

@Composable
fun NoteDetailsScreen(
    detailsViewModel: NoteDetailsViewModel = hiltViewModel(),
    popUpScreen: () -> Unit,
) {
    val state = detailsViewModel.state

    if (state.isClosing) {
        LaunchedEffect(state.isClosing) {
            popUpScreen()
        }
    } else {
        if (state.isLoading) {
            FullScreenLoading()
        } else {
            NoteForm(
                id = state.noteId,
                title = state.title,
                note = state.note,
                editedDate = state.edited,
                createdDate = state.created,
                onNoteChange = detailsViewModel::updateContent,
                onTitleChange = detailsViewModel::updateTitle,
                onDelete = detailsViewModel::delete,
                onExit = detailsViewModel::exit,
            )
        }
    }
}

@Composable
fun NoteForm(
    id: Int?,
    title: String?,
    note: String,
    createdDate: LocalDateTime?,
    editedDate: LocalDateTime?,
    onTitleChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onDelete: () -> Unit,
    onExit: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp,
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                ) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = onExit,
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Exit",
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.background,
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                ) {
                    if (id != null) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
                            onClick = onDelete,
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete",
                            )
                        }
                    }

                    if (editedDate != null) {
                        DateLabel(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Edited ${editedDate.format(Formatter)}",
                        )
                    } else if (createdDate != null) {
                        DateLabel(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Created ${createdDate.format(Formatter)}",
                        )
                    }

                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = "Attach image",
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            NoteField(
                value = title ?: "",
                onValueChange = onTitleChange,
                placeholder = "Title",
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 30.sp),
            )
            NoteField(
                value = note,
                onValueChange = onNoteChange,
                placeholder = "Note",
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
fun NoteField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            if (placeholder != null) {
                Text(
                    text = placeholder,
                    style = textStyle,
                )
            }
        },
        textStyle = textStyle,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
        ),
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
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
private fun DateLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
        style = LocalTextStyle.current.copy(
            fontStyle = FontStyle.Italic,
            fontSize = 12.sp,
        )
    )
}


@Preview
@Composable
fun CreateNoteScreenPreview() {
    NoteAppTheme {
        NoteDetailsScreen(
            popUpScreen = {}
        )
    }
}