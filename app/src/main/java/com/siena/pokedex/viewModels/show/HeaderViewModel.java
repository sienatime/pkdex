package com.siena.pokedex.viewModels.show;

import android.content.Context;
import android.view.View;
import com.siena.pokedex.R;
import com.siena.pokedex.models.persisted.Pokemon;

import static com.siena.pokedex.ResourceUtil.getStringForIdentifier;
import static com.siena.pokedex.ResourceUtil.getPokemonImageId;

/**
 * Created by Siena Aguayo on 6/14/16.
 */
public class HeaderViewModel {
  public String pokeName;
  public String pokeGenus;
  public int numberOfTypes;
  public TypeViewModel firstTypeViewModel;
  public TypeViewModel secondTypeViewModel = null;

  public int secondTypeVisibility;
  public int imageResourceId;
  public int imageVisibility;

  public HeaderViewModel(Pokemon pokemon, Context context) {
    this.pokeName = getStringForIdentifier("pokemon_species_name_", pokemon.getId());
    this.pokeGenus = String.format(context.getString(R.string.genus_format),
        getStringForIdentifier("pokemon_species_genus_", pokemon.getId()));

    this.numberOfTypes = pokemon.getTypes().size();
    this.secondTypeVisibility = this.numberOfTypes == 2 ? View.VISIBLE : View.INVISIBLE;

    this.firstTypeViewModel = new TypeViewModel(pokemon.getTypes().get(0));
    if (pokemon.getTypes().size() > 1) {
      this.secondTypeViewModel = new TypeViewModel(pokemon.getTypes().get(1));
    }

    this.imageResourceId = getPokemonImageId(pokemon);
    this.imageVisibility = imageResourceId > 0 ? View.VISIBLE : View.INVISIBLE;
  }
}
