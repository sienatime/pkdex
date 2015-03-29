package com.siena.pokedex;

import android.database.Cursor;
import com.siena.pokedex.models.Encounter;
import com.siena.pokedex.models.EncounterConditionValueProse;
import com.siena.pokedex.models.EncounterMethod;
import com.siena.pokedex.models.EncounterMethodProse;
import com.siena.pokedex.models.EncounterSlot;
import com.siena.pokedex.models.Location;
import com.siena.pokedex.models.LocationArea;
import com.siena.pokedex.models.LocationAreaProse;
import com.siena.pokedex.models.LocationName;
import com.siena.pokedex.models.Pokemon;
import com.siena.pokedex.models.PokemonType;
import com.siena.pokedex.models.TypeEfficacy;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Siena Aguayo on 2/28/15.
 */
public class PopulateRealm {
  public static void addTypeData(Realm realm, DataAdapter dataAdapter) {
    Cursor cursor = dataAdapter.getData("SELECT pokemon_id, type_id, slot FROM pokemon_types");
    cursor.moveToFirst();
    for (int i = 0; i < cursor.getCount(); i++) {
      PokemonType type = realm.createObject(PokemonType.class);
      type.setPokemonId(cursor.getInt(0));
      type.setTypeId(cursor.getInt(1));
      type.setSlot(cursor.getInt(2));

      Pokemon pokemon = realm.where(Pokemon.class).equalTo("id", type.getPokemonId()).findFirst();

      if (pokemon != null) {
        if (pokemon.getTypes() == null) {
          pokemon.setTypes(new RealmList<PokemonType>());
        }
        pokemon.getTypes().add(type);
      }

      cursor.moveToNext();
    }
    cursor.close();
  }

  public static void addPokemonData(Realm realm, DataAdapter dataAdapter) {
    final Cursor testdata = dataAdapter.getAllPokemonData();
    for (int i = 0; i < 719; i++) {
      Pokemon poke = realm.createObject(Pokemon.class);
      //_id|identifier|species_id|height|weight|base_experience|order|is_default
      poke.setId(testdata.getInt(0));
      poke.setIdentifier(testdata.getString(1));
      poke.setSpeciesId(testdata.getInt(2));
      poke.setHeight(testdata.getInt(3));
      poke.setWeight(testdata.getInt(4));
      poke.setBaseExperience(testdata.getInt(5));
      poke.setOrder(testdata.getInt(6));
      poke.setDefault(testdata.getInt(7) == 1);

      testdata.moveToNext();
    }
  }

  public static void addTypeEfficacy(Realm realm, DataAdapter dataAdapter) {
    //damage_type_id|target_type_id|damage_factor
    Cursor cursor = dataAdapter.getData(
        "SELECT damage_type_id, target_type_id, damage_factor FROM type_efficacy");
    cursor.moveToFirst();
    for (int i = 0; i < cursor.getCount(); i++) {
      TypeEfficacy typeEfficacy = realm.createObject(TypeEfficacy.class);
      typeEfficacy.setDamageTypeId(cursor.getInt(0));
      typeEfficacy.setTargetTypeId(cursor.getInt(1));
      typeEfficacy.setDamageFactor(cursor.getInt(2));
      cursor.moveToNext();
    }
    cursor.close();
  }

  public static void addLocationAreaProse(Realm realm, DataAdapter dataAdapter) {
    Cursor cursor = dataAdapter.getData(
        "SELECT location_area_prose.location_area_id, location_area_prose.local_language_id, "
            + "location_area_prose.name FROM location_area_prose");
    cursor.moveToFirst();

    for (int i = 0; i < cursor.getCount(); i++) {
      LocationAreaProse locationAreaProse = realm.createObject(LocationAreaProse.class);
      locationAreaProse.setLocationAreaId(cursor.getInt(0));
      locationAreaProse.setLocalLanguageId(cursor.getInt(1));
      locationAreaProse.setName(cursor.getString(2) == null ? "" : cursor.getString(2));
      cursor.moveToNext();
    }
    cursor.close();
  }

  public static void addEncounters(Realm realm, DataAdapter dataAdapter) {
    //@PrimaryKey private int id;
    //private int versionId, encounterSlotId, minLevel, maxLevel;
    Cursor cursor = dataAdapter.getData(
        "SELECT encounters.id, encounters.version_id, encounters.encounter_slot_id, "
            + "encounters.min_level, encounters.max_level, encounters.pokemon_id, "
            + "location_areas.id, location_areas.location_id, location_areas.game_index, "
            + "location_areas.identifier, locations.id, locations.region_id, locations.identifier, "
            + "encounter_slots.id, encounter_slots.version_group_id, "
            + "encounter_slots.encounter_method_id, encounter_slots.slot, encounter_slots.rarity, "
            + "encounter_methods.id, encounter_methods.identifier, encounter_methods.`order` "
            + "FROM encounters "
            + "JOIN location_areas on encounters.location_area_id = location_areas.id "
            + "JOIN locations on location_areas.location_id = locations.id "
            + "JOIN encounter_slots on encounter_slots.id = encounters.encounter_slot_id "
            + "JOIN encounter_methods on encounter_methods.id = encounter_slots.encounter_method_id");
    cursor.moveToFirst();
    for (int i = 0; i < cursor.getCount(); i++) {
      Encounter encounter = realm.createObject(Encounter.class);
      encounter.setId(cursor.getInt(0));
      encounter.setVersionId(cursor.getInt(1));
      encounter.setEncounterSlotId(cursor.getInt(2));
      encounter.setMinLevel(cursor.getInt(3));
      encounter.setMaxLevel(cursor.getInt(4));
      encounter.setPokemonId(cursor.getInt(5));

      LocationArea locationArea =
          realm.where(LocationArea.class).equalTo("id", cursor.getInt(6)).findFirst();

      if (locationArea == null) {
        locationArea = realm.createObject(LocationArea.class);
        locationArea.setId(cursor.getInt(6));
        locationArea.setLocationId(cursor.getInt(7));
        locationArea.setGameIndex(cursor.getInt(8));
        locationArea.setIdentifier(cursor.getString(9) == null ? "" : cursor.getString(9));

        Location location =
            realm.where(Location.class).equalTo("id", locationArea.getLocationId()).findFirst();

        if (location == null) {
          location = realm.createObject(Location.class);
          location.setId(cursor.getInt(10));
          location.setRegionId(cursor.getInt(11));
          location.setIdentifier(cursor.getString(12));
        }

        locationArea.setLocation(location);
      }

      encounter.setLocationArea(locationArea);

      EncounterSlot encounterSlot =
          realm.where(EncounterSlot.class).equalTo("id", cursor.getInt(13)).findFirst();

      if (encounterSlot == null) {
        encounterSlot = realm.createObject(EncounterSlot.class);
        encounterSlot.setId(cursor.getInt(13));
        encounterSlot.setVersionGroupId(cursor.getInt(14));
        encounterSlot.setEncounterMethodId(cursor.getInt(15));
        encounterSlot.setSlot(cursor.getInt(16));
        encounterSlot.setRarity(cursor.getInt(17));

        EncounterMethod encounterMethod = realm.where(EncounterMethod.class)
            .equalTo("id", encounterSlot.getEncounterMethodId())
            .findFirst();

        if (encounterMethod == null) {
          encounterMethod = realm.createObject(EncounterMethod.class);
          encounterMethod.setId(cursor.getInt(18));
          encounterMethod.setIdentifier(cursor.getString(19));
          encounterMethod.setOrder(cursor.getInt(20));
        }

        encounterSlot.setEncounterMethod(encounterMethod);
      }

      encounter.setEncounterSlot(encounterSlot);

      Pokemon pokemon = realm.where(Pokemon.class).equalTo("id", cursor.getInt(5)).findFirst();

      if (pokemon != null) {
        if (pokemon.getEncounters() == null) {
          pokemon.setEncounters(new RealmList<Encounter>());
        }
        pokemon.getEncounters().add(encounter);
      }

      cursor.moveToNext();
    }
    cursor.close();
  }

  public static void addLocationNames(Realm realm, DataAdapter dataAdapter) {
    // location_id	local_language_id	name
    Cursor cursor =
        dataAdapter.getData("SELECT location_id, local_language_id, name FROM location_names");
    cursor.moveToFirst();

    for (int i = 0; i < cursor.getCount(); i++) {
      LocationName locationName = realm.createObject(LocationName.class);
      locationName.setLocationId(cursor.getInt(0));
      locationName.setLocalLanguageId(cursor.getInt(1));
      locationName.setName(cursor.getString(2) == null ? "" : cursor.getString(2));
      cursor.moveToNext();
    }
    cursor.close();
  }

  public static void addEncouterMethodProse(Realm realm, DataAdapter dataAdapter) {
    // encounter_method_id	local_language_id	name
    Cursor cursor = dataAdapter.getData(
        "SELECT encounter_method_id, local_language_id, name FROM encounter_method_prose");
    cursor.moveToFirst();

    for (int i = 0; i < cursor.getCount(); i++) {
      EncounterMethodProse encounterMethodProse = realm.createObject(EncounterMethodProse.class);
      encounterMethodProse.setEncounterMethodId(cursor.getInt(0));
      encounterMethodProse.setLocalLanguageId(cursor.getInt(1));
      encounterMethodProse.setName(cursor.getString(2) == null ? "" : cursor.getString(2));
      cursor.moveToNext();
    }
    cursor.close();
  }

  public static void addEncounterConditionValueProse(Realm realm, DataAdapter dataAdapter) {
    // encounter_method_id	local_language_id	name
    Cursor cursor = dataAdapter.getData(
        "SELECT encounter_condition_value_id, local_language_id, name FROM encounter_condition_value_prose");
    cursor.moveToFirst();

    for (int i = 0; i < cursor.getCount(); i++) {
      EncounterConditionValueProse encounterConditionValueProse =
          realm.createObject(EncounterConditionValueProse.class);
      encounterConditionValueProse.setEncounterConditionValueId(cursor.getInt(0));
      encounterConditionValueProse.setLocalLanguageId(cursor.getInt(1));
      encounterConditionValueProse.setName(cursor.getString(2) == null ? "" : cursor.getString(2));
      cursor.moveToNext();
    }
    cursor.close();
  }

  public static void addEncounterConditionValueId(Realm realm, DataAdapter dataAdapter) {
    // encounter_method_id	local_language_id	name
    Cursor cursor = dataAdapter.getData(
        "SELECT encounter_id, encounter_condition_value_id FROM encounter_condition_value_map");
    cursor.moveToFirst();

    for (int i = 0; i < cursor.getCount(); i++) {
      Encounter encounter =
          realm.where(Encounter.class).equalTo("id", cursor.getInt(0)).findFirst();
      encounter.setEncounterConditionId(cursor.getInt(1));
      cursor.moveToNext();
    }
    cursor.close();
  }
}
