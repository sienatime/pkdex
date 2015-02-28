package com.siena.pokedex.models;

import io.realm.RealmObject;

/**
 * Created by Siena Aguayo on 2/28/15.
 */
public class TypeName extends RealmObject {
  private int typeId;
  private int localLanguageId;
  private String name;

  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
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
