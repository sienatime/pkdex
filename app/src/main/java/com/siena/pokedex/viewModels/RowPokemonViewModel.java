package com.siena.pokedex.viewModels;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import com.siena.pokedex.PokemonUtil;
import com.siena.pokedex.R;
import com.siena.pokedex.fragments.PokeInfoFragment;
import com.siena.pokedex.models.Pokemon;

import static com.siena.pokedex.PokemonUtil.formatId;
import static com.siena.pokedex.PokemonUtil.getPokeString;
import static com.siena.pokedex.PokemonUtil.getPokemonImageId;

/**
 * Created by Siena Aguayo on 6/14/16.
 */
public class RowPokemonViewModel {
  public String pokeId;
  public String pokeName;
  public int imageResourceId;
  public int imageVisibility;
  public View.OnClickListener onClickListener;

  public RowPokemonViewModel(final Pokemon pokemon, final Context context) {
    this.pokeId = formatId(pokemon);
    this.pokeName = getPokeString(pokemon.getId(), "pokemon_species_name_");
    this.imageResourceId = getPokemonImageId(pokemon);
    this.imageVisibility = imageResourceId > 0 ? View.VISIBLE : View.INVISIBLE;

    this.onClickListener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        Fragment fragment = new PokeInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PokemonUtil.POKEMON_ID_KEY, pokemon.getId());
        fragment.setArguments(bundle);
        ((ActionBarActivity) context).getSupportFragmentManager()
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit();
      }
    };
  }
}
