package com.beworld.task1.di

import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.fragment.app.Fragment
import com.beworld.task1.data.repository.CameraRepositoryImpl
import com.beworld.task1.domain.repository.CameraRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(FragmentComponent::class)
object CameraModule {

    @Provides
    @FragmentScoped
    fun provideCameraController(cameraFragment: Fragment): LifecycleCameraController {
        val cameraController = LifecycleCameraController(cameraFragment.requireContext())
        cameraController.bindToLifecycle(cameraFragment)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        return cameraController
    }

    @Provides
    @FragmentScoped
    fun provideCameraRepository(
        cameraController: LifecycleCameraController
    ): CameraRepository = CameraRepositoryImpl(cameraController)
}