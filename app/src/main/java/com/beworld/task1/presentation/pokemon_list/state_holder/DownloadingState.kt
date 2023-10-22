package com.beworld.task1.presentation.pokemon_list.state_holder

data class DownloadingState(
    val isDownloading: Boolean = false,
    val downloadingResult: Boolean? = null
)
