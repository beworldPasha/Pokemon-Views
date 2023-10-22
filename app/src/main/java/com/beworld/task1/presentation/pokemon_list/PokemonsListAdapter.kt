package com.beworld.task1.presentation.pokemon_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.beworld.task1.R
import com.beworld.task1.common.Constants
import com.beworld.task1.databinding.PokemonsListItemBinding
import com.beworld.task1.domain.model.Pokemon
import com.beworld.task1.domain.model.PokemonDetail
import com.beworld.task1.pokemosso.Pokemosso
import okhttp3.internal.immutableListOf
import okhttp3.internal.toImmutableList

class PokemonsListAdapter(
    private val callback: AdapterCallback
) :
    RecyclerView.Adapter<PokemonsListAdapter.PokemonsListViewHolder>() {
    class PokemonsListViewHolder(val binding: PokemonsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface AdapterCallback {
        fun onItemClick(position: Int, pokemonName: String)
        fun onLongPressed(pokemon: Pokemon, view: View)
    }

    private var pokemons = immutableListOf<Pokemon>()

    @SuppressLint("NotifyDataSetChanged")
    fun setPokemonsList(otherPokemons: List<Pokemon>) {
        pokemons = otherPokemons.toImmutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PokemonsListItemBinding
            .inflate(inflater, parent, false)

        return PokemonsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonsListViewHolder, position: Int) {
        val pokemon = pokemons[position]
        holder.binding.tvPokemonName.text = pokemon.name

        Pokemosso.get()
            .load(Constants.FRONT_DEFAULT_IMG_URL + pokemon.id + Constants.IMG_FORMAT)
            .into(holder.binding.ivPokemonImage, object : Pokemosso.Callback {
                override fun onError() {
                    holder.binding.progressBar.visibility = View.GONE
                    holder.binding.ivPokemonImage.setImageResource(R.drawable.ic_broken_image)
                }

                override fun onComplete() {
                    holder.binding.progressBar.visibility = View.GONE
                    holder.binding.ivPokemonImage.visibility = View.VISIBLE
                }

                override fun onLoading() {
                    holder.binding.progressBar.visibility = View.VISIBLE
                    holder.binding.ivPokemonImage.visibility = View.GONE
                }
            })

        holder.itemView.setOnClickListener {
            callback.onItemClick(position, pokemon.name)
        }

        if (pokemon.fromRemote) {
            holder.itemView.setOnLongClickListener {
                callback.onLongPressed(pokemon, holder.itemView)
                true
            }
        }
    }


    override fun getItemCount(): Int = pokemons.size
}