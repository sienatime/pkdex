package com.siena.pokedex.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.siena.pokedex.R;
import com.siena.pokedex.databinding.RowPokemonBinding;
import com.siena.pokedex.models.Pokemon;
import com.siena.pokedex.viewModels.RowPokemonViewModel;
import io.realm.RealmResults;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class PokemonRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private RealmResults<Pokemon> pokemon;
  private Context context;

  public PokemonRecyclerViewAdapter(Context context, RealmResults<Pokemon> pokes) {
    this.pokemon = pokes;
    this.context = context;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RowPokemonBinding binding =
        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_pokemon,
            parent, false);
    return new PokeRowViewHolder(binding);
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
    Pokemon pokemon = this.pokemon.get(position);
    RowPokemonViewModel viewModel = new RowPokemonViewModel(pokemon, context);
    ((PokeRowViewHolder) viewHolder).binding.setViewModel(viewModel);
  }

  @Override public int getItemCount() {
    return pokemon.size();
  }

  public static class PokeRowViewHolder extends RecyclerView.ViewHolder {
    public RowPokemonBinding binding;

    public PokeRowViewHolder(RowPokemonBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
