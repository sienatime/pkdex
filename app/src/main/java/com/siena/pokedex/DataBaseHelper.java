package com.siena.pokedex;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class DataBaseHelper extends SQLiteAssetHelper {

  private static final String DATABASE_NAME = "pokedex.sqlite";
  private static final int DATABASE_VERSION = 2;

  public DataBaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
}
