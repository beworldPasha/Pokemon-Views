package com.beworld.task1.presentation.camera

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.beworld.task1.R
import com.beworld.task1.databinding.FragmentCameraBinding
import com.beworld.task1.domain.repository.CameraRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var cameraRepository: CameraRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .visibility = View.GONE

        cameraRepository.bindToPreview(binding.cameraPreview)

        binding.btnFlipCamera.setOnClickListener {
            cameraRepository.flipCamera()
        }

        binding.btnTakePhoto.setOnClickListener {
            lifecycleScope.launch {
                cameraRepository.takePhoto(requireContext().contentResolver, requireContext())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .visibility = View.VISIBLE
    }
}