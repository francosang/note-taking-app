package com.task.noteapp.appinitializers

import android.app.Application
import com.task.noteapp.BuildConfig
import com.task.noteapp.commons.logger.Logger
import javax.inject.Inject

class LoggerInitializer @Inject constructor(
    private val logger: Logger
) : AppInitializer {
    override fun init(application: Application) {
        logger.init(BuildConfig.DEBUG)
    }
}
