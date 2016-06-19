package com.siena.pokedex.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.siena.pokedex.R;
import com.siena.pokedex.databinding.RowIndexPokemonBinding;
import com.siena.pokedex.models.persisted.Pokemon;
import com.siena.pokedex.viewModels.index.PokemonViewModel;
import io.realm.RealmResults;

/**
 * Created by Siena Aguayo on 12/27/14.
 */
public class PokemonIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private RealmResults<Pokemon> pokemon;
  private Context context;

  public PokemonIndexAdapter(Context context, RealmResults<Pokemon> pokes) {
    this.pokemon = pokes;
    this.context = context;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RowIndexPokemonBinding binding =
        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_index_pokemon,
            parent, false);
    return new PokeRowViewHolder(binding);
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
    Pokemon pokemon = this.pokemon.get(position);
    PokemonViewModel viewModel = new PokemonViewModel(pokemon, context);
    ((PokeRowViewHolder) viewHolder).binding.setViewModel(viewModel);
  }

  @Override public int getItemCount() {
    return pokemon.size();
  }

  public static class PokeRowViewHolder extends RecyclerView.ViewHolder {
    public RowIndexPokemonBinding binding;

    public PokeRowViewHolder(RowIndexPokemonBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
