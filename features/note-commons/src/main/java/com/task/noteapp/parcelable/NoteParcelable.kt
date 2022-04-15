package com.task.noteapp.parcelable

import android.os.Parcelable
import com.task.noteapp.domain.Note
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

// TODO remove this
@Parcelize
data class NoteParcelable(
    val id: Int,
    val title: String? = null,
    val content: String,
    val image: String? = null,
    val created: LocalDateTime,
    val edited: LocalDateTime?  = null,
) : Parcelable


fun List<Note>.toParcelables() = this.map { note ->
    NoteParcelable(
        id = note.id ?: 0,
        title = note.title,
        content = note.content,
        image = note.image,
        created = note.created,
        edited = note.edited,
    )
}
