package com.siena.pokedex.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class PokemonInfoAdapter extends BaseAdapter {
  private List<Row> rows;
  @Override public int getCount() {
    return rows.size();
  }

  @Override public Row getItem(int position) {
    return rows.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    return rows.get(position).getView(convertView, parent);
  }

  @Override public int getItemViewType(int position) {
    return rows.get(position).getType();
  }

  @Override public int getViewTypeCount() {
    return 1;
  }
}
