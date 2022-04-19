package com.task.noteapp.ui.component

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val Formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

@Composable
fun DateLabel(
    modifier: Modifier = Modifier,
    edited: LocalDateTime?,
    created: LocalDateTime?,
    fontSize: TextUnit = 12.sp,
    fontStyle: FontStyle? = FontStyle.Italic,
) {
    val formattedDate = when {
        edited != null -> {
            "Edited ${edited.format(Formatter)}"
        }
        created != null -> {
            "Created ${created.format(Formatter)}"
        }
        else -> {
            null
        }
    }

    if (formattedDate != null) {
        Text(
            modifier = modifier,
            text = formattedDate,
            style = LocalTextStyle.current.copy(
                fontStyle = fontStyle,
                fontSize = fontSize,
            )
        )
    }
}