package com.siena.pokedex.viewModels.show;

import android.content.res.Resources;
import com.siena.pokedex.PokedexApp;
import com.siena.pokedex.PokemonUtil;

/**
 * Created by Siena Aguayo on 6/14/16.
 */
public class VersionHeaderViewModel {
  public int versionNameId;
  public int color;

  public VersionHeaderViewModel(int versionId) {
    Resources res = PokedexApp.getInstance().getResources();
    this.versionNameId = res.getIdentifier("version_name_" + versionId, "string",
        PokedexApp.getInstance().getPackageName());
    this.color = PokemonUtil.getVersionColor(versionId);
  }
}
