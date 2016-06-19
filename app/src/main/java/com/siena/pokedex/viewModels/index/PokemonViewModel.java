package com.siena.pokedex.viewModels.index;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import com.siena.pokedex.PokedexApp;
import com.siena.pokedex.R;
import com.siena.pokedex.fragments.PokeInfoFragment;
import com.siena.pokedex.models.persisted.Pokemon;

import static com.siena.pokedex.ResourceUtil.getPokemonImageId;
import static com.siena.pokedex.ResourceUtil.getStringForIdentifier;

/**
 * Created by Siena Aguayo on 6/14/16.
 */
public class PokemonViewModel {
  public int pokeId;
  public String pokeName;
  public int imageResourceId;
  public int imageVisibility;
  public View.OnClickListener onClickListener;

  public PokemonViewModel(final Pokemon pokemon, final Context context) {
    this.pokeId = pokemon.getId();
    this.pokeName = getStringForIdentifier("pokemon_species_name_", pokemon.getId());
    this.imageResourceId = getPokemonImageId(pokemon);
    this.imageVisibility = imageResourceId > 0 ? View.VISIBLE : View.INVISIBLE;

    this.onClickListener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        Fragment fragment = new PokeInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PokedexApp.BUNDLE_KEY_POKEMON_ID, pokemon.getId());
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
