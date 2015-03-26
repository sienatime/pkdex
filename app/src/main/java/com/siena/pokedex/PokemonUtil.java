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
    return RES.getIdentifier(getImageName(pokemon.getId()), "drawable", PACKAGE_NAME);
  }

  public static String getImageName(int id) {
    return "sprite_" + Integer.toString(id);
  }

  public static String nameToKey(String name) {
    return name.replace("-", "_");
  }

  public static String getLocalizedPokeName(Pokemon pokemon) {
    int nameId = RES.getIdentifier(nameToKey(pokemon.getIdentifier()), "string", PACKAGE_NAME);
    return RES.getString(nameId);
  }

  public static int getTypeColor(int id) {
    int colorId = RES.getIdentifier("type" + Integer.toString(id), "color", PACKAGE_NAME);
    return RES.getColor(colorId);
  }

  public static String formatId(Pokemon pokemon) {
    return String.format(APP.getString(R.string.number_format), pokemon.getId());
  }

  public static String consolidateLevels(int minLevel, int maxLevel) {
    Resources res = PokedexApp.getInstance().getResources();
    if (minLevel == maxLevel) {
      return String.format(res.getString(R.string.level_singular), Integer.toString(minLevel));
    } else {
      return String.format(res.getString(R.string.level_range), Integer.toString(minLevel),
          Integer.toString(maxLevel));
    }
  }
}
