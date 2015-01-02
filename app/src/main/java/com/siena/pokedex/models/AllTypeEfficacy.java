package com.siena.pokedex.models;

import java.util.List;

/**
 * Created by Siena Aguayo on 1/2/15.
 */
public class AllTypeEfficacy {
  private List<Pokemon.Type> weakTo;
  private List<Pokemon.Type> damagedNormallyBy;
  private List<Pokemon.Type> resistantTo;
  private List<Pokemon.Type> immuneTo;

  public AllTypeEfficacy(List<Pokemon.Type> weakTo, List<Pokemon.Type> damagedNormallyBy,
      List<Pokemon.Type> resistantTo, List<Pokemon.Type> immuneTo) {
    this.weakTo = weakTo;
    this.damagedNormallyBy = damagedNormallyBy;
    this.resistantTo = resistantTo;
    this.immuneTo = immuneTo;
  }

  public List<Pokemon.Type> getWeakTo() {
    return weakTo;
  }

  public List<Pokemon.Type> getDamagedNormallyBy() {
    return damagedNormallyBy;
  }

  public List<Pokemon.Type> getResistantTo() {
    return resistantTo;
  }

  public List<Pokemon.Type> getImmuneTo() {
    return immuneTo;
  }
}
