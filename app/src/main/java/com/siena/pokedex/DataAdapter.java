package com.siena.pokedex;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class DataAdapter {
  protected static final String TAG = "DataAdapter";
  private final String ENGLISH_ID = "9";

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
}
