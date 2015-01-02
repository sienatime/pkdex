package com.siena.pokedex.models;

/**
 * Created by Siena Aguayo on 1/2/15.
 */
public class SingleTypeEfficacy {
  //damage_type_id|target_type_id|damage_factor
  private int damageTypeId;
  private int damageFactor;

  public SingleTypeEfficacy(int damageTypeId, int damageFactor) {
    this.damageTypeId = damageTypeId;
    this.damageFactor = damageFactor;
  }

  public int getDamageTypeId() {
    return damageTypeId;
  }

  public int getDamageFactor() {
    return damageFactor;
  }
}
