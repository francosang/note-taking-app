package com.task.noteapp.note.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.task.noteapp.note.result.contrat.OpenDocumentWithPersistablePermission
import com.task.noteapp.ui.component.NoteImage
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
            NoteScaffold(
                id = state.noteId,
                title = state.title,
                note = state.note,
                image = state.image,
                editedDate = state.edited,
                createdDate = state.created,
                onImagePicked = detailsViewModel::updateImage,
                onImageRemoved = detailsViewModel::removeImage,
                onNoteChange = detailsViewModel::updateContent,
                onTitleChange = detailsViewModel::updateTitle,
                onDelete = detailsViewModel::delete,
                onExit = detailsViewModel::exit,
            )
        }
    }
}

@Composable
fun NoteScaffold(
    id: Int?,
    title: String?,
    note: String,
    image: String?,
    createdDate: LocalDateTime?,
    editedDate: LocalDateTime?,
    onTitleChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onImageRemoved: () -> Unit,
    onImagePicked: (Uri) -> Unit,
    onDelete: () -> Unit,
    onExit: () -> Unit,
) {
    Scaffold(
        topBar = {
            NoteTopBar(onExit = onExit)
        },
        bottomBar = {
            NoteBottomBar(
                id = id,
                createdDate = createdDate,
                editedDate = editedDate,
                onDelete = onDelete,
                onImagePicked = onImagePicked,
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            NoteForm(
                title = title,
                note = note,
                image = image,
                onImageRemoved = onImageRemoved,
                onNoteChange = onNoteChange,
                onTitleChange = onTitleChange,
            )
        }
    }
}

@Composable
fun NoteForm(
    title: String?,
    note: String,
    image: String?,
    onTitleChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onImageRemoved: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        NoteField(
            value = title ?: "",
            onValueChange = onTitleChange,
            placeholder = "Title",
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(fontSize = 30.sp),
        )

        if (image != null) {
            Box {
                NoteImage(
                    localUri = Uri.parse(image),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )

                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(32.dp),
                    color = MaterialTheme.colors.background
                ) {
                    IconButton(
                        onClick = onImageRemoved,
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Remove image",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        }

        NoteField(
            value = note,
            onValueChange = onNoteChange,
            placeholder = "Note",
            modifier = Modifier
                .fillMaxSize(),
        )
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

@Composable
private fun NoteTopBar(
    onExit: () -> Unit
) {
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
}

@Composable
private fun NoteBottomBar(
    id: Int?,
    createdDate: LocalDateTime?,
    editedDate: LocalDateTime?,
    onImagePicked: (Uri) -> Unit,
    onDelete: () -> Unit,
) {
    val context = LocalContext.current
    val contentPickerLauncher = rememberLauncherForActivityResult(
        contract = OpenDocumentWithPersistablePermission(),
        onResult = {
            if (it != null) {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                onImagePicked(it)
            }
        },
    )

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
                onClick = {
                    contentPickerLauncher.launch("image/*")
                }
            ) {
                Icon(
                    Icons.Filled.Image,
                    contentDescription = "Pick image",
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

@Preview
@Composable
fun CreateNoteScreenPreview() {
    NoteAppTheme {
        NoteDetailsScreen(
            popUpScreen = {}
        )
    }
}
