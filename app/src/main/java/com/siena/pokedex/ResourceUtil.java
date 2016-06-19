package com.siena.pokedex;

import android.content.res.Resources;
import com.siena.pokedex.models.persisted.Pokemon;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class ResourceUtil {
  private static final PokedexApp APP = PokedexApp.getInstance();
  private static final String PACKAGE_NAME = APP.getPackageName();
  private static final Resources RES = APP.getResources();

  public static int getPokemonImageId(Pokemon pokemon) {
    String imageName = "sprite_" + Integer.toString(pokemon.getId());
    return RES.getIdentifier(imageName, "drawable", PACKAGE_NAME);
  }

  public static String getStringForIdentifier(String identifier, int id) {
    String key = identifier + Integer.toString(id);
    int nameId = RES.getIdentifier(key, "string", PACKAGE_NAME);
    return nameId > 0 ? RES.getString(nameId) : null;
  }

  public static int getTypeColor(int id) {
    int colorId = RES.getIdentifier("type" + Integer.toString(id), "color", PACKAGE_NAME);
    return RES.getColor(colorId);
  }
}
