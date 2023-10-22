package com.beworld.task1.presentation.photo_list.state_holder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beworld.task1.common.Resource
import com.beworld.task1.data.local.photo_provider.Photo
import com.beworld.task1.domain.repository.PhotoRepository
import com.beworld.task1.domain.use_case.get_photos.GetPhotosUseCase
import com.beworld.task1.photo_provider.PhotoProvider
import com.beworld.task1.presentation.pokemon_list.state_holder.PokemonsListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val photoRepository: PhotoRepository
) : ViewModel() {
    private val _photoListStateHolder: MutableLiveData<PhotosState> by lazy {
        MutableLiveData<PhotosState>()
    }
    val photoListStateHolder: LiveData<PhotosState>
        get() = _photoListStateHolder

    fun loadPhotoList() {
        getPhotosUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _photoListStateHolder.value =
                        PhotosState(photos = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    _photoListStateHolder.value =
                        PhotosState(error = result.message ?: "An expected error")
                }

                is Resource.Loading -> {
                    _photoListStateHolder.value = PhotosState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun removePhoto(photo: Photo, callback: PhotoProvider.RemoveCallback) {
        viewModelScope.launch {
            photoRepository.removePhoto(photo, callback)
        }
    }
}