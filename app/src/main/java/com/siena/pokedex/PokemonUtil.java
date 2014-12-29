package com.siena.pokedex;

import android.content.res.Resources;
import com.siena.pokedex.models.Pokemon;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class PokemonUtil {
  private static final PokedexApp APP = PokedexApp.getInstance();
  private static final String PACKAGE_NAME = APP.getPackageName();
  private static final Resources RES = APP.getResources();
  public static final String POKEMON_ID_KEY = "pokemonId";

  public static int getPokemonImageId(Pokemon pokemon) {
    return RES.getIdentifier(pokemon.getImageName(), "drawable", PACKAGE_NAME);
  }

  public static String getLocalizedPokeName(Pokemon pokemon) {
    int nameId = RES.getIdentifier(pokemon.nameToKey(), "string", PACKAGE_NAME);
    return RES.getString(nameId);
  }

  public static String formatId(Pokemon pokemon) {
    return String.format(APP.getString(R.string.number_format), pokemon.getId());
  }
}
