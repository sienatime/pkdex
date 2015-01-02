package com.siena.pokedex;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.siena.pokedex.models.AllTypeEfficacy;
import com.siena.pokedex.models.Pokemon;
import com.siena.pokedex.models.SingleTypeEfficacy;
import java.io.IOException;
import java.util.ArrayList;
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
    Cursor cursor = getData("SELECT identifier FROM pokemon WHERE _id = " + intString);
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

  public AllTypeEfficacy getTypeEfficacy(List<Pokemon.Type> types) {
    Integer id = types.get(0).getId();
    Cursor cursor = getData(
        "SELECT * FROM type_efficacy WHERE target_type_id = " + id + " ORDER BY damage_type_id");

    cursor.moveToFirst();
    ArrayList<SingleTypeEfficacy> efficacies = new ArrayList<SingleTypeEfficacy>();
    while (!cursor.isAfterLast()) {
      SingleTypeEfficacy efficacy =
          new SingleTypeEfficacy(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));
      efficacies.add(efficacy);
      cursor.moveToNext();
    }
    cursor.close();

    List<Pokemon.Type> weakTo = new ArrayList<>();
    List<Pokemon.Type> immuneTo = new ArrayList<>();
    List<Pokemon.Type> resistantTo = new ArrayList<>();
    List<Pokemon.Type> damagedNormallyBy = new ArrayList<>();

    for (SingleTypeEfficacy efficacy : efficacies) {
      int damageTypeId = efficacy.getDamageTypeId();
      switch (efficacy.getDamageFactor()) {
        case 0:
          immuneTo.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
          break;
        case 25:
          resistantTo.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
          break;
        case 50:
          resistantTo.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
          break;
        case 100:
          damagedNormallyBy.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
          break;
        case 200:
          weakTo.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
          break;
        case 400:
          weakTo.add(new Pokemon.Type(damageTypeId, getTypeById(damageTypeId)));
          break;
        default:
          Log.v("type efficacy",
              "unknown damage factor: " + Integer.toString(efficacy.getDamageFactor()));
      }
    }

    return new AllTypeEfficacy(weakTo, damagedNormallyBy, resistantTo, immuneTo);
  }
}
