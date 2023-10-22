package com.beworld.task1.presentation.photo_list.state_holder

import com.beworld.task1.data.local.photo_provider.Photo

data class PhotosState(
    val isLoading: Boolean = false,
    val photos: List<Photo> = emptyList(),
    val error: String = ""
)
