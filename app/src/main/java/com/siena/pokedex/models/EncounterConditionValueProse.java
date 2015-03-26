package com.siena.pokedex.models;

import io.realm.RealmObject;

/**
 * Created by Siena Aguayo on 3/14/15.
 */
public class EncounterConditionValueProse extends RealmObject {
  private int encounterConditionValueId, localLanguageId;
  private String name;

  public EncounterConditionValueProse() {
  }

  public int getEncounterConditionValueId() {
    return encounterConditionValueId;
  }

  public void setEncounterConditionValueId(int encounterConditionValueId) {
    this.encounterConditionValueId = encounterConditionValueId;
  }

  public int getLocalLanguageId() {
    return localLanguageId;
  }

  public void setLocalLanguageId(int localLanguageId) {
    this.localLanguageId = localLanguageId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
