package com.task.noteapp.appinitializers

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}
