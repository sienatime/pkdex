package com.siena.pokedex.viewModels;

import android.content.Context;
import android.view.View;
import com.siena.pokedex.R;
import com.siena.pokedex.models.Pokemon;

import static com.siena.pokedex.PokemonUtil.getPokeString;
import static com.siena.pokedex.PokemonUtil.getPokemonImageId;

/**
 * Created by Siena Aguayo on 6/14/16.
 */
public class PokeInfoHeaderViewModel {
  public String pokeName;
  public String pokeGenus;
  public int numberOfTypes;
  public TypeViewHolder firstTypeViewHolder;
  public TypeViewHolder secondTypeViewHolder = null;

  public int secondTypeVisibility;
  public int imageResourceId;
  public int imageVisibility;

  public PokeInfoHeaderViewModel(Pokemon pokemon, Context context) {
    this.pokeName = getPokeString(pokemon.getId(), "pokemon_species_name_");
    this.pokeGenus = String.format(context.getString(R.string.genus_format),
        getPokeString(pokemon.getId(), "pokemon_species_genus_"));

    this.numberOfTypes = pokemon.getTypes().size();
    this.secondTypeVisibility = this.numberOfTypes == 2 ? View.VISIBLE : View.INVISIBLE;

    this.firstTypeViewHolder = new TypeViewHolder(pokemon.getTypes().get(0));
    if (pokemon.getTypes().size() > 1) {
      this.secondTypeViewHolder = new TypeViewHolder(pokemon.getTypes().get(1));
    }

    this.imageResourceId = getPokemonImageId(pokemon);
    this.imageVisibility = imageResourceId > 0 ? View.VISIBLE : View.INVISIBLE;
  }
}
