package com.beworld.task1.presentation.photo_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.beworld.task1.R
import com.beworld.task1.data.local.photo_provider.Photo
import com.beworld.task1.databinding.BottomSheetPhotoViewerBinding
import com.beworld.task1.databinding.FragmentPhotosBinding
import com.beworld.task1.photo_provider.PhotoProvider
import com.beworld.task1.pokemosso.Pokemosso
import com.beworld.task1.presentation.photo_list.state_holder.PhotosViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosFragment : Fragment() {
    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private var _bindingBottomSheet: BottomSheetPhotoViewerBinding? = null
    private val bindingBottomSheet get() = _bindingBottomSheet!!

    private val viewModel: PhotosViewModel by viewModels()
    private lateinit var photoListAdapter: PhotoListAdapter

    private var bottomSheet: BottomSheetDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        viewModel.loadPhotoList()

        _bindingBottomSheet = BottomSheetPhotoViewerBinding.inflate(layoutInflater)

        bottomSheet = context?.let { BottomSheetDialog(it) }
        bottomSheet?.setContentView(bindingBottomSheet.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoListAdapter = PhotoListAdapter(object : PhotoListAdapter.ItemCallback {
            override fun openClick(photo: Photo) {
                Pokemosso.get()
                    .load(photo.uri)
                    .resize(340, 540)
                    .into(bindingBottomSheet.ivPhoto, object : Pokemosso.Callback {
                        override fun onError() {
                            bindingBottomSheet.tvError.visibility = View.VISIBLE

                            bindingBottomSheet.progressBar.visibility = View.GONE
                            bindingBottomSheet.tvError.text = "Cant load Image."
                            bindingBottomSheet.itemContainer.visibility = View.INVISIBLE
                        }

                        override fun onComplete() {
                            bindingBottomSheet.itemContainer.visibility = View.VISIBLE

                            bindingBottomSheet.progressBar.visibility = View.GONE
                            bindingBottomSheet.tvError.visibility = View.GONE
                        }

                        override fun onLoading() {
                            bindingBottomSheet.progressBar.visibility = View.VISIBLE

                            bindingBottomSheet.itemContainer.visibility = View.INVISIBLE
                            bindingBottomSheet.tvError.visibility = View.GONE
                        }
                    })

                bindingBottomSheet.tvPhotoName.text = photo.name
                bindingBottomSheet.tvPhotoDate.text = photo.date

                bottomSheet?.show()
            }

            override fun removeClick(photo: Photo, position: Int) {
                context?.let {
                    AlertDialog.Builder(it).apply {
                        setTitle("Access remove")
                        setMessage("R u sure that u want to delete this photo?")
                        setPositiveButton("Yeah") { _, _ ->
                            viewModel.removePhoto(photo, object : PhotoProvider.RemoveCallback {
                                override fun onSuccess() {
                                    photoListAdapter.removePhoto(position)

                                    if (photoListAdapter.itemCount == 0)
                                        viewModel.loadPhotoList()
                                }

                                override fun onError() {
                                    AlertDialog.Builder(it).apply {
                                        setTitle("Error")
                                    }
                                }

                            })
                        }
                        setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }.create().show()
                }
            }
        })
        binding.rvPhotoList.adapter = photoListAdapter
        viewModel.photoListStateHolder.observe(viewLifecycleOwner) {
            if (it.error.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE

                binding.tvError.text = it.error
            } else if (!it.isLoading) {
                photoListAdapter.setPhotoList(it.photos)

                binding.progressBar.visibility = View.GONE
                binding.rvPhotoList.visibility = View.VISIBLE
            }
        }

        binding.toolbar.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.camera_top_bar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)



        binding.fabCamera.setOnClickListener {
            findNavController().navigate(R.id.action_photosFragment_to_cameraFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _bindingBottomSheet = null
        bottomSheet = null
    }
}