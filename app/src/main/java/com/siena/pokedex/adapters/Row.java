package com.siena.pokedex.adapters;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public interface Row {
  public int getType();
  public View getView(View convertView, ViewGroup parent);
}
