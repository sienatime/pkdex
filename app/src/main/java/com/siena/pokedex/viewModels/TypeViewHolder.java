package com.siena.pokedex.viewModels;

import com.siena.pokedex.models.PokemonType;

import static com.siena.pokedex.PokemonUtil.getPokeString;
import static com.siena.pokedex.PokemonUtil.getTypeColor;

/**
 * Created by Siena Aguayo on 6/14/16.
 */
public class TypeViewHolder {
  public int color;
  public String name;

  public TypeViewHolder(PokemonType type) {
    this.name = getPokeString(type.getTypeId(), "type_");
    this.color = getTypeColor(type.getTypeId());
  }
}
