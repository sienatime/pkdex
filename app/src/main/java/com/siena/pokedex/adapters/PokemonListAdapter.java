package com.siena.pokedex.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.siena.pokedex.DataAdapter;
import com.siena.pokedex.PokedexApp;
import com.siena.pokedex.R;
import com.siena.pokedex.bus.ShowPokemonInfoEvent;
import com.siena.pokedex.models.Pokemon;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import static com.siena.pokedex.PokemonUtil.formatId;
import static com.siena.pokedex.PokemonUtil.getLocalizedPokeName;
import static com.siena.pokedex.PokemonUtil.getPokemonImageId;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class PokemonListAdapter extends BaseAdapter {
  private List<Row> rows = new ArrayList<>();
  private List<Pokemon> pokemon;
  private Context context;
  private final int POKEMON_ROW = 100;
  @Inject Bus bus;

  public PokemonListAdapter(Context context) {
    this.context = context;
    PokedexApp.getInstance().inject(this);

    DataAdapter mDbHelper = new DataAdapter(context);
    mDbHelper.createDatabase();
    mDbHelper.open();

    Cursor testdata = mDbHelper.getPokemonData();

    for (int i = 0; i < 719; i++ ) {
      Pokemon poke = new Pokemon(testdata.getInt(0), testdata.getString(1));
      rows.add(new PokeRow(POKEMON_ROW, poke, context, bus));
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
    private Picasso picasso;
    private Bus bus;

    public PokeRow(int rowType, Pokemon pokemon, Context context, Bus bus) {
      this.pokemon = pokemon;
      this.rowType = rowType;
      this.context = context;
      this.picasso = Picasso.with(context);
      this.bus = bus;
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

      viewHolder.container.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          bus.post(new ShowPokemonInfoEvent(pokemon.getId()));
        }
      });

      viewHolder.pokeId.setText(formatId(pokemon));
      viewHolder.pokeName.setText(getLocalizedPokeName(pokemon));

      int imageId = getPokemonImageId(pokemon);
      if (imageId > 0) {
        viewHolder.pokeImage.setVisibility(View.VISIBLE);
        picasso.load(getPokemonImageId(pokemon))
            .into(viewHolder.pokeImage);
      } else {
        Log.e("listadapter", "couldn't find image for id " + Integer.toString(pokemon.getId()));
        viewHolder.pokeImage.setVisibility(View.INVISIBLE);
      }

      return convertView;
    }

    static class ViewHolder {
      @InjectView(R.id.row_poke_id) TextView pokeId;
      @InjectView(R.id.row_poke_name) TextView pokeName;
      @InjectView(R.id.row_poke_image) ImageView pokeImage;
      @InjectView(R.id.poke_row) RelativeLayout container;

      public ViewHolder(View source) {
        ButterKnife.inject(this, source);
      }
    }
  }
}
