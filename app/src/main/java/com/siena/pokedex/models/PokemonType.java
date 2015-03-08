package com.siena.pokedex.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Siena Aguayo on 2/28/15.
 */
public class PokemonType extends RealmObject {
  @PrimaryKey private int pokemonId;
  private int typeId, slot;

  public PokemonType() {
  }

  public int getPokemonId() {
    return pokemonId;
  }

  public void setPokemonId(int pokemonId) {
    this.pokemonId = pokemonId;
  }

  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  public int getSlot() {
    return slot;
  }

  public void setSlot(int slot) {
    this.slot = slot;
  }
}
