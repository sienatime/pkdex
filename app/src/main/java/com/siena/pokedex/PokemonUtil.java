package com.siena.pokedex;

import android.content.res.Resources;
import com.siena.pokedex.models.persisted.Pokemon;

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

  public static String getStringForIdentifier(int id, String identifier) {
    String key = identifier + Integer.toString(id);
    int nameId = RES.getIdentifier(key, "string", PACKAGE_NAME);
    return nameId > 0 ? RES.getString(nameId) : null;
  }

  public static int getTypeColor(int id) {
    int colorId = RES.getIdentifier("type" + Integer.toString(id), "color", PACKAGE_NAME);
    return RES.getColor(colorId);
  }
}
