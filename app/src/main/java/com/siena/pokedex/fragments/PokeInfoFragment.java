package com.siena.pokedex.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.siena.pokedex.PokemonUtil;
import com.siena.pokedex.R;
import com.siena.pokedex.adapters.PokemonShowAdapter;
import com.siena.pokedex.models.persisted.Pokemon;
import io.realm.Realm;

import static com.siena.pokedex.PokemonUtil.getPokeString;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class PokeInfoFragment extends Fragment {
  @InjectView(R.id.poke_info_listview) ListView listView;
  private ActionBar actionBar;

  public PokeInfoFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_pokemon_show, container, false);
    ButterKnife.inject(this, rootView);
    actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    int id = getArguments().getInt(PokemonUtil.POKEMON_ID_KEY);

    Realm realm = Realm.getDefaultInstance();
    Pokemon poke = realm.where(Pokemon.class).equalTo("id", id).findFirst();
    actionBar.setTitle(getPokeString(poke.getId(), "pokemon_species_name_"));
    listView.setAdapter(new PokemonShowAdapter(poke));
    return rootView;
  }

  @Override public void onDetach() {
    actionBar.setTitle(R.string.app_name);
    super.onDetach();
  }
}
