package com.siena.pokedex;

import android.database.Cursor;
import com.siena.pokedex.models.Encounter;
import com.siena.pokedex.models.LocationArea;
import com.siena.pokedex.models.LocationAreaProse;
import com.siena.pokedex.models.Pokemon;
import com.siena.pokedex.models.PokemonSpeciesName;
import com.siena.pokedex.models.PokemonType;
import com.siena.pokedex.models.TypeEfficacy;
import com.siena.pokedex.models.TypeName;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Siena Aguayo on 2/28/15.
 */
public class PopulateRealm {
  public static void addEverything(Realm realm, DataAdapter dataAdapter) {
    addPokemonData(realm, dataAdapter);
    addTypeData(realm, dataAdapter);
    addTypeNames(realm, dataAdapter);
    addSpeciesNames(realm, dataAdapter);
    addTypeEfficacy(realm, dataAdapter);
    addEncounters(realm, dataAdapter);
  }

  public static void addSpeciesNames(Realm realm, DataAdapter dataAdapter) {
    Cursor cursor = dataAdapter.getData(
        "SELECT pokemon_species_id, local_language_id, name, genus FROM pokemon_species_names");
    cursor.moveToFirst();
    for (int i = 0; i < cursor.getCount(); i++) {
      PokemonSpeciesName pokemonSpeciesName = realm.createObject(PokemonSpeciesName.class);
      pokemonSpeciesName.setPokemonSpeciesId(cursor.getInt(0));
      pokemonSpeciesName.setLocalLanguageId(cursor.getInt(1));
      pokemonSpeciesName.setName(cursor.getString(2) != null ? cursor.getString(2) : "");
      String genus = cursor.getString(3);
      if (genus != null) {
        pokemonSpeciesName.setGenus(genus);
      } else {
        pokemonSpeciesName.setGenus("");
      }
      cursor.moveToNext();
    }
    cursor.close();
  }

  public static void addTypeNames(Realm realm, DataAdapter dataAdapter) {
    Cursor cursor = dataAdapter.getData("SELECT type_id, local_language_id, name FROM type_names");
    cursor.moveToFirst();
    for (int i = 0; i < cursor.getCount(); i++) {
      TypeName typeName = realm.createObject(TypeName.class);
      typeName.setTypeId(cursor.getInt(0));
      typeName.setLocalLanguageId(cursor.getInt(1));
      typeName.setName(cursor.getString(2));
      cursor.moveToNext();
    }
    cursor.close();
  }

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
            + "location_areas.identifier FROM encounters "
            + "JOIN location_areas on encounters.location_area_id = location_areas.id");
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
      }

      encounter.setLocationArea(locationArea);

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
}
