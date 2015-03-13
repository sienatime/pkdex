package com.siena.pokedex.models;

import io.realm.RealmObject;

/**
 * Created by Siena Aguayo on 1/2/15.
 */
public class TypeEfficacy extends RealmObject {
  //damage_type_id|target_type_id|damage_factor
  private int damageTypeId, targetTypeId, damageFactor;

  public TypeEfficacy() {
  }

  public TypeEfficacy(int damageTypeId, int damageFactor) {
    this.damageFactor = damageFactor;
    this.damageTypeId = damageTypeId;
  }

  public int getDamageTypeId() {
    return damageTypeId;
  }

  public void setDamageTypeId(int damageTypeId) {
    this.damageTypeId = damageTypeId;
  }

  public int getTargetTypeId() {
    return targetTypeId;
  }

  public void setTargetTypeId(int targetTypeId) {
    this.targetTypeId = targetTypeId;
  }

  public int getDamageFactor() {
    return damageFactor;
  }

  public void setDamageFactor(int damageFactor) {
    this.damageFactor = damageFactor;
  }
}
