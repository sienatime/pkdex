package com.siena.pokedex.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.siena.pokedex.R;
import com.siena.pokedex.models.Pokemon;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import static com.siena.pokedex.PokemonUtil.getLocalizedPokeName;
import static com.siena.pokedex.PokemonUtil.getPokemonImageId;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class PokemonInfoAdapter extends BaseAdapter {
  private List<Row> rows = new ArrayList<>();
  private Pokemon pokemon;
  private final int HEADER_ROW = 100;
  private Context context;

  public PokemonInfoAdapter(Context context, Pokemon pokemon) {
    this.pokemon = pokemon;
    this.context = context;

    rows.add(new HeaderRow(HEADER_ROW, pokemon, context));
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

  public static class HeaderRow implements Row {
    private Pokemon pokemon;
    private int rowType;
    private Context context;
    private Picasso picasso;

    public HeaderRow(int rowType, Pokemon pokemon, Context context) {
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
        convertView = LayoutInflater.from(context).inflate(R.layout.row_poke_header, parent, false);
        viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      viewHolder.pokeName.setText(getLocalizedPokeName(pokemon));
      viewHolder.pokeGenus.setText(
          String.format(context.getString(R.string.genus_format), pokemon.getGenus()));

      int numberOfTypes = pokemon.getTypes().size();

      if (numberOfTypes > 0) {
        viewHolder.type1.setText(pokemon.getTypes().get(0).getLocalizedName());

        if (numberOfTypes == 2) {
          viewHolder.type2.setVisibility(View.VISIBLE);
          viewHolder.type2.setText(pokemon.getTypes().get(1).getLocalizedName());
        } else {
          viewHolder.type2.setVisibility(View.GONE);
        }
      }

      int imageId = getPokemonImageId(pokemon);
      if (imageId > 0) {
        viewHolder.pokeImage.setVisibility(View.VISIBLE);
        picasso.load(getPokemonImageId(pokemon)).into(viewHolder.pokeImage);
      } else {
        Log.e("listadapter", "couldn't find image for id " + Integer.toString(pokemon.getId()));
        viewHolder.pokeImage.setVisibility(View.INVISIBLE);
      }

      return convertView;
    }

    static class ViewHolder {
      @InjectView(R.id.header_poke_name) TextView pokeName;
      @InjectView(R.id.header_poke_genus) TextView pokeGenus;
      @InjectView(R.id.pokemon_type_1) TextView type1;
      @InjectView(R.id.pokemon_type_2) TextView type2;
      @InjectView(R.id.header_poke_image) ImageView pokeImage;

      public ViewHolder(View source) {
        ButterKnife.inject(this, source);
      }
    }
  }
}
