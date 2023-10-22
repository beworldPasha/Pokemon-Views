package com.beworld.task1.domain.repository

import android.content.ContentResolver
import android.content.Context
import androidx.camera.view.PreviewView

interface CameraRepository {
    fun bindToPreview(preview: PreviewView)
    fun flipCamera()
    suspend fun takePhoto(
        contentResolver: ContentResolver,
        context: Context
    )
}