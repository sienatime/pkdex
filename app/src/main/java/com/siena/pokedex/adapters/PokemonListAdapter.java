package com.siena.pokedex.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
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
import com.siena.pokedex.PokemonUtil;
import com.siena.pokedex.R;
import com.siena.pokedex.fragments.PokeInfoFragment;
import com.siena.pokedex.models.Pokemon;
import com.squareup.picasso.Picasso;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

import static com.siena.pokedex.PokemonUtil.formatId;
import static com.siena.pokedex.PokemonUtil.getPokeString;
import static com.siena.pokedex.PokemonUtil.getPokemonImageId;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class PokemonListAdapter extends BaseAdapter {
  private List<Row> rows = new ArrayList<>();
  private final int POKEMON_ROW = 100;

  public PokemonListAdapter(Context context) {
    Realm realm = Realm.getInstance(context);
    RealmResults<Pokemon> pokes = realm.allObjectsSorted(Pokemon.class, "id", true);

    for (Pokemon poke : pokes) {
      rows.add(new PokeRow(POKEMON_ROW, poke, context));
    }
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

    public PokeRow(int rowType, Pokemon pokemon, Context context) {
      this.pokemon = pokemon;
      this.rowType = rowType;
      this.context = context;
      this.picasso = Picasso.with(context);
    }

    @Override public int getType() {
      return rowType;
    }

    @Override public View getView(View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        convertView =
            LayoutInflater.from(context).inflate(R.layout.row_pokemon_item, parent, false);
        viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      viewHolder.container.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Fragment fragment = new PokeInfoFragment();
          Bundle bundle = new Bundle();
          bundle.putInt(PokemonUtil.POKEMON_ID_KEY, pokemon.getId());
          fragment.setArguments(bundle);
          ((ActionBarActivity) context).getSupportFragmentManager()
              .beginTransaction()
              .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
              .replace(R.id.container, fragment)
              .addToBackStack(null)
              .commit();
        }
      });

      viewHolder.pokeId.setText(formatId(pokemon));
      viewHolder.pokeName.setText(getPokeString(pokemon.getId(), "pokemon_species_name_"));

      int imageId = getPokemonImageId(pokemon);
      if (imageId > 0) {
        viewHolder.pokeImage.setVisibility(View.VISIBLE);
        picasso.load(imageId).into(viewHolder.pokeImage);
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
