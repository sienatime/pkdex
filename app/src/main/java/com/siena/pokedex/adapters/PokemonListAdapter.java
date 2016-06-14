package com.siena.pokedex.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import io.realm.RealmResults;

import static com.siena.pokedex.PokemonUtil.formatId;
import static com.siena.pokedex.PokemonUtil.getPokeString;
import static com.siena.pokedex.PokemonUtil.getPokemonImageId;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class PokemonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private RealmResults<Pokemon> pokemon;
  private Context context;
  private Picasso picasso;

  public PokemonListAdapter(Context context, RealmResults<Pokemon> pokes) {
    this.pokemon = pokes;
    this.context = context;
    this.picasso = Picasso.with(context);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.row_pokemon_item, parent, false);
    PokeRowViewHolder vh = new PokeRowViewHolder(v);
    return vh;
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
    final PokeRowViewHolder pokeRowViewHolder = (PokeRowViewHolder) viewHolder;
    final Pokemon pokemon = this.pokemon.get(position);
    pokeRowViewHolder.container.setOnClickListener(new View.OnClickListener() {
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

    pokeRowViewHolder.pokeId.setText(formatId(pokemon));
    pokeRowViewHolder.pokeName.setText(getPokeString(pokemon.getId(), "pokemon_species_name_"));

    int imageId = getPokemonImageId(pokemon);
    if (imageId > 0) {
      pokeRowViewHolder.pokeImage.setVisibility(View.VISIBLE);
      picasso.load(imageId).into(pokeRowViewHolder.pokeImage);
    } else {
      Log.e("listadapter", "couldn't find image for id " + Integer.toString(pokemon.getId()));
      pokeRowViewHolder.pokeImage.setVisibility(View.INVISIBLE);
    }
  }

  @Override public int getItemCount() {
    return pokemon.size();
  }

  public static class PokeRowViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.row_poke_id) TextView pokeId;
    @InjectView(R.id.row_poke_name) TextView pokeName;
    @InjectView(R.id.row_poke_image) ImageView pokeImage;
    @InjectView(R.id.poke_row) RelativeLayout container;

    public PokeRowViewHolder(View source) {
      super(source);
      ButterKnife.inject(this, source);
    }
  }
}
