package com.siena.pokedex.bus;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class ShowPokemonInfoEvent {
  private int id;

  public ShowPokemonInfoEvent(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
