package com.beworld.task1.presentation.pokemon_info

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.beworld.task1.R
import com.beworld.task1.databinding.FragmentInfoBinding
import com.beworld.task1.domain.model.PokemonDetail
import com.beworld.task1.pokemosso.Pokemosso
import com.beworld.task1.presentation.shared_view_model.PokemonsSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonDetailViewModel by viewModels()
    private val sharedViewModel: PokemonsSharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel.getName()?.let { viewModel.fetchPokemon(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pokemonsListStateHolder.observe(viewLifecycleOwner) { state ->
            if (state.error.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE

                binding.tvError.text = state.error
            } else if (!state.isLoading) {
                state.pokemon?.let { pokemon ->
                    val longListener = View.OnLongClickListener {
                        showDownloadMenuWithResults(pokemon, binding.ivPokemonImage)
                        true
                    }

                    Pokemosso.get()
                        .load(pokemon.photoUrl)
                        .into(binding.ivPokemonImage, object : Pokemosso.Callback {
                            override fun onError() {
                                _binding?.apply {
                                    imageProgressBar.visibility = View.GONE
                                    ivPokemonImage.setImageResource(R.drawable.ic_broken_image)
                                    ivPokemonImage.visibility = View.VISIBLE
                                }
                            }

                            override fun onComplete() {
                                _binding?.apply {
                                    imageProgressBar.visibility = View.GONE
                                    ivPokemonImage.visibility = View.VISIBLE
                                    ivPokemonImage.setOnLongClickListener(longListener)
                                }
                            }

                            override fun onLoading() {}
                        })

                    binding.tvExperience.text = "Experience: ${pokemon.experience}"
                    binding.tvName.text = "Name: ${pokemon.name}"
                    binding.tvHeight.text = "Height: ${pokemon.height}"
                    binding.tvWeight.text = "Weight: ${pokemon.weight}"
                }

                binding.progressBar.visibility = View.GONE
                binding.itemContainer.visibility = View.VISIBLE
            }
        }
    }

    private fun showDownloadMenuWithResults(pokemon: PokemonDetail, item: View) {
        val menu = PopupMenu(context, item)
        menu.inflate(R.menu.photo_menu)
        menu.setOnMenuItemClickListener {
            var message: AlertDialog? = null
            fun cancelMessage() {
                message?.dismiss()
                message = null
            }
            Pokemosso.get()
                .load(pokemon.photoUrl)
                .intoDevice(
                    photoName = pokemon.name,
                    callback = object : Pokemosso.Callback {
                        override fun onLoading() {
                            cancelMessage()
                            message = AlertDialog.Builder(context).apply {
                                setTitle("Loading")
                                setView(ProgressBar(context))
                                setMessage("")
                            }.show()
                        }

                        override fun onError() {
                            cancelMessage()
                            message = AlertDialog.Builder(context).apply {
                                setTitle("Error Loading")
                                setMessage("")
                            }.show()
                        }

                        override fun onComplete() {
                            cancelMessage()
                            message = AlertDialog.Builder(context).apply {
                                setTitle("Success Loading")
                                setMessage("")
                            }.show()
                        }
                    })
            true
        }
        menu.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}