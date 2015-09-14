package com.siena.pokedex.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class Pokemon extends RealmObject {
  @PrimaryKey private int id;
  private String identifier;
  private int speciesId, height, weight, baseExperience, order;
  private boolean isDefault;
  private RealmList<PokemonType> types;
  private RealmList<Encounter> encounters;
  private RealmList<ConsolidatedEncounter> consolidatedEncounters;
  // dumb workaround until i can get unique values from Realm
  private String encounterVersions;

  //_id|identifier|species_id|height|weight|base_experience|order|is_default
  public Pokemon() {

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

  public int getSpeciesId() {
    return speciesId;
  }

  public void setSpeciesId(int speciesId) {
    this.speciesId = speciesId;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public int getBaseExperience() {
    return baseExperience;
  }

  public void setBaseExperience(int baseExperience) {
    this.baseExperience = baseExperience;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public boolean isDefault() {
    return isDefault;
  }

  public void setDefault(boolean isDefault) {
    this.isDefault = isDefault;
  }

  public int getId() {
    return id;
  }

  public RealmList<PokemonType> getTypes() {
    return types;
  }

  public void setTypes(RealmList<PokemonType> types) {
    this.types = types;
  }

  public RealmList<Encounter> getEncounters() {
    return encounters;
  }

  public void setEncounters(RealmList<Encounter> encounters) {
    this.encounters = encounters;
  }

  public RealmList<ConsolidatedEncounter> getConsolidatedEncounters() {
    return consolidatedEncounters;
  }

  public void setConsolidatedEncounters(RealmList<ConsolidatedEncounter> consolidatedEncounters) {
    this.consolidatedEncounters = consolidatedEncounters;
  }

  public String getEncounterVersions() {
    return encounterVersions;
  }

  public void setEncounterVersions(String encounterVersions) {
    this.encounterVersions = encounterVersions;
  }
}
