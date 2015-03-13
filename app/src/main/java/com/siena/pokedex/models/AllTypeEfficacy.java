package com.siena.pokedex.models;

import io.realm.RealmList;

/**
 * Created by Siena Aguayo on 1/2/15.
 */
public class AllTypeEfficacy {
  private RealmList<PokemonType> weakTo;
  private RealmList<PokemonType> damagedNormallyBy;
  private RealmList<PokemonType> resistantTo;
  private RealmList<PokemonType> immuneTo;
  
  public AllTypeEfficacy(RealmList<PokemonType> weakTo, RealmList<PokemonType> damagedNormallyBy,
      RealmList<PokemonType> resistantTo, RealmList<PokemonType> immuneTo) {
    this.weakTo = weakTo;
    this.damagedNormallyBy = damagedNormallyBy;
    this.resistantTo = resistantTo;
    this.immuneTo = immuneTo;
  }
  
  public RealmList<PokemonType> getWeakTo() {
    return weakTo;
  }
  
  public RealmList<PokemonType> getDamagedNormallyBy() {
    return damagedNormallyBy;
  }
  
  public RealmList<PokemonType> getResistantTo() {
    return resistantTo;
  }
  
  public RealmList<PokemonType> getImmuneTo() {
    return immuneTo;
  }
}
