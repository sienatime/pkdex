package com.siena.pokedex.viewModels.show;

import com.siena.pokedex.models.persisted.PokemonType;

import static com.siena.pokedex.PokemonUtil.getPokeString;
import static com.siena.pokedex.PokemonUtil.getTypeColor;

/**
 * Created by Siena Aguayo on 6/14/16.
 */
public class TypeViewModel {
  public int color;
  public String name;

  public TypeViewModel(PokemonType type) {
    this.name = getPokeString(type.getTypeId(), "type_");
    this.color = getTypeColor(type.getTypeId());
  }
}
