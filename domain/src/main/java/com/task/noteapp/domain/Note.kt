package com.task.noteapp.domain

import java.time.LocalDateTime

data class Note(
    val id: Int,
    val title: String? = null,
    val content: String,
    val image: String? = null,
    val created: LocalDateTime,
    val edited: LocalDateTime? = null,
)
