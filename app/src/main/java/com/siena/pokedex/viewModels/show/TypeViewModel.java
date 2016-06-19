package com.siena.pokedex.viewModels.show;

import com.siena.pokedex.models.persisted.PokemonType;

import static com.siena.pokedex.ResourceUtil.getStringForIdentifier;
import static com.siena.pokedex.ResourceUtil.getTypeColor;

/**
 * Created by Siena Aguayo on 6/14/16.
 */
public class TypeViewModel {
  public int color;
  public String name;

  public TypeViewModel(PokemonType type) {
    this.name = getStringForIdentifier("type_", type.getTypeId());
    this.color = getTypeColor(type.getTypeId());
  }
}
