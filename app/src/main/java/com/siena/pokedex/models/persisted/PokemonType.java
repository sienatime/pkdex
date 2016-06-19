package com.siena.pokedex.models.persisted;

import io.realm.RealmObject;

/**
 * Created by Siena Aguayo on 2/28/15.
 */
public class PokemonType extends RealmObject {
  private int pokemonId, typeId, slot;

  public PokemonType() {
  }

  public PokemonType(int typeId) {
    this.typeId = typeId;
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
