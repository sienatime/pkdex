package com.siena.pokedex.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Siena Aguayo on 3/25/15.
 */
public class EncounterMethod extends RealmObject {
  // id	identifier	order
  @PrimaryKey private int id;
  private String identifier;
  private int order;

  public EncounterMethod() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }
}
