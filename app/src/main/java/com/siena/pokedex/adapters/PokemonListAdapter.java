package com.siena.pokedex.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.siena.pokedex.DataAdapter;
import com.siena.pokedex.R;
import com.siena.pokedex.models.Pokemon;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class PokemonListAdapter extends BaseAdapter {
  private List<Row> rows = new ArrayList<>();
  private List<Pokemon> pokemon;
  private Context context;
  private final int POKEMON_ROW = 100;

  public PokemonListAdapter(Context context) {
    this.context = context;

    DataAdapter mDbHelper = new DataAdapter(context);
    mDbHelper.createDatabase();
    mDbHelper.open();

    Cursor testdata = mDbHelper.getPokemonData();

    for (int i = 0; i < 200; i++ ) {
      Pokemon poke = new Pokemon(testdata.getInt(0), testdata.getString(1));
      rows.add(new PokeRow(POKEMON_ROW, poke, context));
      testdata.moveToNext();
    }

    mDbHelper.close();
  }

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

  public static class PokeRow implements Row {
    private Pokemon pokemon;
    private int rowType;
    private Context context;

    public PokeRow(int rowType, Pokemon pokemon, Context context) {
      this.pokemon = pokemon;
      this.rowType = rowType;
      this.context = context;
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_pokemon_item, parent, false);
        viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      viewHolder.pokeId.setText(Integer.toString(pokemon.getId()));
      viewHolder.pokeName.setText(pokemon.getName());
      //picasso.load(me.getAvatarUrl())
      //    .fit()
      //    .centerCrop()
      //    .placeholder(R.drawable.missing)
      //    .into(viewHolder.profileHeaderAvatar);
      return convertView;
    }

    static class ViewHolder {
      @InjectView(R.id.row_poke_id) TextView pokeId;
      @InjectView(R.id.row_poke_name) TextView pokeName;
      @InjectView(R.id.row_poke_image) ImageView pokeImage;

      public ViewHolder(View source) {
        ButterKnife.inject(this, source);
      }

      @OnClick(R.id.poke_row) public void onRowClicked(View view) {
        Log.v("hey", "you clicked a pokemon");
      }
    }
  }
}
