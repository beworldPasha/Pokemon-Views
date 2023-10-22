package com.beworld.task1.presentation.pokemon_list

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
import androidx.navigation.fragment.findNavController
import com.beworld.task1.R
import com.beworld.task1.databinding.FragmentPokemonsBinding
import com.beworld.task1.domain.model.Pokemon
import com.beworld.task1.presentation.pokemon_list.state_holder.PokemonsViewModel
import com.beworld.task1.presentation.shared_view_model.PokemonsSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonsFragment : Fragment() {
    private var _binding: FragmentPokemonsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonsViewModel by viewModels()
    private val sharedViewModel: PokemonsSharedViewModel by activityViewModels()

    private var message: AlertDialog? = null
    private fun cancelMessage() {
        message?.dismiss()
        message = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokemonsListAdapter = PokemonsListAdapter(object : PokemonsListAdapter.AdapterCallback {
            override fun onItemClick(position: Int, pokemonName: String) {
                sharedViewModel.setName(pokemonName)

                findNavController()
                    .navigate(R.id.action_pokemonsFragment_to_pokemonDetailFragment)
            }

            override fun onLongPressed(pokemon: Pokemon, view: View) {
                showDownloadMenu(pokemon, view)
            }
        })

        binding.rvPokemonsList.adapter = pokemonsListAdapter
        viewModel.pokemonsListStateHolder.observe(viewLifecycleOwner) {
            if (it.error.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE

                binding.tvError.text = it.error
            } else if (!it.isLoading) {
                pokemonsListAdapter.setPokemonsList(it.pokemons)

                binding.progressBar.visibility = View.GONE
                binding.rvPokemonsList.visibility = View.VISIBLE
            }
        }

        viewModel.downloadStateHolder.observe(viewLifecycleOwner) {
            setDownloadingListener(it.isDownloading, it.downloadingResult)
        }
    }

    private fun setDownloadingListener(isDownloading: Boolean, result: Boolean?) {
        if (isDownloading) {
            cancelMessage()
            message = AlertDialog.Builder(context).apply {
                setTitle("Loading")
                setView(ProgressBar(context))
                setMessage("")
            }.show()
        }
        result?.let { isCorrect ->
            cancelMessage()
            message = if (isCorrect) {
                AlertDialog.Builder(context).apply {
                    setTitle("Success Loading")
                    setMessage("")
                }
            } else {
                AlertDialog.Builder(context).apply {
                    setTitle("Error Loading")
                    setMessage("")
                }
            }.show()
        }
    }

    private fun showDownloadMenu(pokemon: Pokemon, item: View) {
        val menu = PopupMenu(context, item)
        menu.inflate(R.menu.list_item_menu)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.download -> viewModel.savePokemon(pokemon.name)
                R.id.downloadWithImage -> {

                }
            }
            true
        }
        menu.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        cancelMessage()
        _binding = null
    }
}