package com.siena.pokedex.database.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class DataAdapter {
  protected static final String TAG = "DataAdapter";
  private SQLiteDatabase db;
  private DataBaseHelper dbHelper;

  public DataAdapter(Context context) {
    dbHelper = new DataBaseHelper(context);
    db = dbHelper.getReadableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Cursor getData(String sql) {
    try {
      Cursor cursor = db.rawQuery(sql, null);
      if (cursor != null) {
        cursor.moveToNext();
      }
      return cursor;
    } catch (SQLException exception) {
      Log.e(TAG, "getData >>" + exception.toString());
      throw exception;
    }
  }

  public Cursor getAllPokemonData() {
    return getData("SELECT * FROM pokemon");
  }

}
