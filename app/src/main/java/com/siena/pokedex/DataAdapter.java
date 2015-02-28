package com.siena.pokedex;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.siena.pokedex.models.Encounter;
import com.siena.pokedex.models.SingleTypeEfficacy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class DataAdapter {
  protected static final String TAG = "DataAdapter";
  private final String ENGLISH_ID = "9";
  private final String Y_ID = "24";
  private final String BLACK_2_ID = "21";
  private final String BLACK_ID = "17";
  private final String HEART_GOLD_ID = "15";
  private final String DIAMOND_ID = "12";

  private final Context mContext;
  private SQLiteDatabase mDb;
  private DataBaseHelper mDbHelper;

  public DataAdapter(Context context) {
    this.mContext = context;
    mDbHelper = new DataBaseHelper(mContext);
  }

  public DataAdapter createDatabase() throws SQLException {
    try {
      mDbHelper.createDataBase();
    } catch (IOException mIOException) {
      Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
      throw new Error("UnableToCreateDatabase");
    }
    return this;
  }

  public DataAdapter open() throws SQLException {
    try {
      mDbHelper.openDataBase();
      mDbHelper.close();
      mDb = mDbHelper.getReadableDatabase();
    } catch (SQLException mSQLException) {
      Log.e(TAG, "open >>" + mSQLException.toString());
      throw mSQLException;
    }
    return this;
  }

  public void close() {
    mDbHelper.close();
  }

  public long getNumberOfRows(String table, String selection, String[] selectionArgs) {
    return DatabaseUtils.queryNumEntries(mDb, table, selection,
        selectionArgs);
  }

  public Cursor getData(String sql) {
    try {
      Cursor mCur = mDb.rawQuery(sql, null);
      if (mCur != null) {
        mCur.moveToNext();
      }
      return mCur;
    } catch (SQLException mSQLException) {
      Log.e(TAG, "getData >>" + mSQLException.toString());
      throw mSQLException;
    }
  }

  public Cursor getAllPokemonData() {
    return getData("SELECT * FROM pokemon");
  }

  public List<Integer> getPokemonTypeData(int id) {
    String intString = Integer.toString(id);

    Cursor cursor = getData(
        "SELECT type_id FROM pokemon_types WHERE pokemon_id = " + intString + " ORDER BY slot");
    cursor.moveToFirst();
    ArrayList<Integer> types = new ArrayList<Integer>();
    while (!cursor.isAfterLast()) {
      types.add(cursor.getInt(0));
      cursor.moveToNext();
    }
    cursor.close();

    return types;
  }

  public String getTypeById(Integer id) {
    Cursor cursor = getData("SELECT name FROM type_names WHERE local_language_id = "
        + ENGLISH_ID
        + " AND type_id = "
        + id);
    String result = cursor.getString(0);
    cursor.close();
    return result;
  }

  public String getIdentifierById(int id) {
    String intString = Integer.toString(id);
    Cursor cursor = getData("SELECT identifier FROM pokemon WHERE id = " + intString);
    String result = cursor.getString(0);
    cursor.close();
    return result;
  }

  public String getGenusById(int id) {
    // select * from pokemon_species_names where local_language_id = 9 and pokemon_species_id = 1;
    String intString = Integer.toString(id);
    Cursor cursor = getData("SELECT genus FROM pokemon_species_names WHERE pokemon_species_id = "
        + intString
        + " AND local_language_id = "
        + ENGLISH_ID);
    String result = cursor.getString(0);
    cursor.close();
    return result;
  }

  //public AllTypeEfficacy getTypeEfficacy(List<Pokemon.Type> types) {
  //  List<List<SingleTypeEfficacy>> allTypeEfficaciesList = new ArrayList<>();
  //  for (Pokemon.Type type : types) {
  //    Integer id = type.getId();
  //    Cursor cursor = getData(
  //        "SELECT damage_type_id, damage_factor FROM type_efficacy WHERE target_type_id = "
  //            + id
  //            + " ORDER BY damage_type_id");
  //
  //    cursor.moveToFirst();
  //    ArrayList<SingleTypeEfficacy> efficacies = new ArrayList<>();
  //    while (!cursor.isAfterLast()) {
  //      SingleTypeEfficacy efficacy = new SingleTypeEfficacy(cursor.getInt(0), cursor.getInt(1));
  //      efficacies.add(efficacy);
  //      cursor.moveToNext();
  //    }
  //    cursor.close();
  //    allTypeEfficaciesList.add(efficacies);
  //  }
  //
  //  List<SingleTypeEfficacy> finalEfficacy;
  //
  //  if (allTypeEfficaciesList.size() > 1) {
  //    finalEfficacy = consolidateTypeEfficacy(allTypeEfficaciesList);
  //  } else {
  //    finalEfficacy = allTypeEfficaciesList.get(0);
  //  }
  //
  //  List<Pokemon.Type> weakTo = new ArrayList<>();
  //  List<Pokemon.Type> immuneTo = new ArrayList<>();
  //  List<Pokemon.Type> resistantTo = new ArrayList<>();
  //  List<Pokemon.Type> damagedNormallyBy = new ArrayList<>();
  //
  //  for (SingleTypeEfficacy efficacy : finalEfficacy) {
  //    int damageTypeId = efficacy.getDamageTypeId();
  //    switch (efficacy.getDamageFactor()) {
  //      case 0:
  //        immuneTo.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
  //        break;
  //      case 25:
  //        resistantTo.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
  //        break;
  //      case 50:
  //        resistantTo.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
  //        break;
  //      case 100:
  //        damagedNormallyBy.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
  //        break;
  //      case 200:
  //        weakTo.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
  //        break;
  //      case 400:
  //        weakTo.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
  //        break;
  //      default:
  //        Log.v("type efficacy",
  //            "unknown damage factor: " + Integer.toString(efficacy.getDamageFactor()));
  //    }
  //  }
  //
  //  return new AllTypeEfficacy(weakTo, damagedNormallyBy, resistantTo, immuneTo);
  //}

  private List<SingleTypeEfficacy> consolidateTypeEfficacy(
      List<List<SingleTypeEfficacy>> allTypeEfficaciesList) {
    assert (allTypeEfficaciesList.get(0).size() == allTypeEfficaciesList.get(1).size());

    List<SingleTypeEfficacy> finalEfficacy = new ArrayList<>();

    for (int i = 0; i < allTypeEfficaciesList.get(0).size(); i++) {
      SingleTypeEfficacy damageType1 = allTypeEfficaciesList.get(0).get(i);
      SingleTypeEfficacy damageType2 = allTypeEfficaciesList.get(1).get(i);

      assert (damageType1.getDamageTypeId() == damageType2.getDamageTypeId());
      int combinedDamage = damageType1.getDamageFactor() * damageType2.getDamageFactor() / 100;
      SingleTypeEfficacy combinedTypeEfficacy =
          new SingleTypeEfficacy(damageType1.getDamageTypeId(), combinedDamage);
      finalEfficacy.add(combinedTypeEfficacy);
    }

    return finalEfficacy;
  }

  public List<Encounter> getEncounters(int id) {
    String intString = Integer.toString(id);

    Cursor cursor = getData(
        "select v_encounter.version_id, location_names.name, location_area_prose.name, v_encounter.min_level, v_encounter.max_level, v_encounter.rarity, encounter_method_prose.name, v_encounter._id from v_encounter \n"
            + "join location_names on v_encounter.location_id = location_names.location_id "
            + "join location_area_prose on v_encounter.location_area_id = location_area_prose.location_area_id "
            + "join encounter_method_prose on v_encounter.methodId = encounter_method_prose.encounter_method_id "
            + "where v_encounter.pokemon_id = "
            + intString
            + " and location_names.local_language_id = "
            + ENGLISH_ID
            + " and location_area_prose.local_language_id = "
            + ENGLISH_ID
            + " and encounter_method_prose.local_language_id = "
            + ENGLISH_ID
            + ";");

    cursor.moveToFirst();
    HashMap<Integer, Encounter> encounterMap = new HashMap<>();
    while (!cursor.isAfterLast()) {
      int versionId = cursor.getInt(0);
      String locationName = cursor.getString(1);
      String locationArea = cursor.getString(2);

      //calculate levels
      int minLevel = cursor.getInt(3);
      int maxLevel = cursor.getInt(4);
      String levels = null;
      if (minLevel != maxLevel) {
        levels = Integer.toString(minLevel) + "-" + Integer.toString(maxLevel);
      } else {
        levels = Integer.toString(minLevel);
      }
      int rate = cursor.getInt(5);
      String method = cursor.getString(6);
      Integer encounterId = cursor.getInt(7);
      Encounter encounter =
          new Encounter(versionId, locationName, method, levels, rate, locationArea);
      encounterMap.put(encounterId, encounter);
      cursor.moveToNext();
    }
    cursor.close();

    encounterMap = addEncounterConditions(intString, encounterMap);
    ArrayList<Encounter> encounters = new ArrayList<>(encounterMap.values());
    Collections.sort(encounters, new Comparator<Encounter>() {
      @Override public int compare(Encounter e1, Encounter e2) {
        return e1.getVersionId() - e2.getVersionId(); // Ascending
      }
    });
    return encounters;
  }

  private HashMap<Integer, Encounter> addEncounterConditions(String idString,
      HashMap<Integer, Encounter> encounterMap) {
    Cursor cursor = getData(
        "select encounters.id, encounter_condition_value_prose.name from encounters\n"
            + "join encounter_condition_value_map on encounters.id = encounter_condition_value_map.encounter_id\n"
            + "join encounter_condition_value_prose on encounter_condition_value_prose.encounter_condition_value_id = encounter_condition_value_map.encounter_condition_value_id\n"
            + "where encounters.pokemon_id = "
            + idString
            + " and encounter_condition_value_prose.local_language_id = "
            + ENGLISH_ID
            + ";");
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Integer encounterId = cursor.getInt(0);
      String encounterCondition = cursor.getString(1);

      Encounter encounter = encounterMap.get(encounterId);
      if (encounter != null) {
        encounter.setCondition(encounterCondition);
      } else {
        Log.e("encounter condition", "encounter "
            + Integer.toString(encounterId)
            + " was not in map for encounter condition "
            + encounterCondition);
      }

      cursor.moveToNext();
    }
    cursor.close();

    return encounterMap;
  }
}
