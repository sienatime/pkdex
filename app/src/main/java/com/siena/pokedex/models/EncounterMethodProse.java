package com.siena.pokedex.models;

import io.realm.RealmObject;

/**
 * Created by Siena Aguayo on 3/25/15.
 */
public class EncounterMethodProse extends RealmObject {
  // encounter_method_id	local_language_id	name
  private int encounterMethodId, localLanguageId;
  private String name;

  public EncounterMethodProse() {
  }

  public int getEncounterMethodId() {
    return encounterMethodId;
  }

  public void setEncounterMethodId(int encounterMethodId) {
    this.encounterMethodId = encounterMethodId;
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
