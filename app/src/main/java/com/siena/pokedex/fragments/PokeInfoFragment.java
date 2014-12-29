package com.siena.pokedex.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.siena.pokedex.DataAdapter;
import com.siena.pokedex.PokemonUtil;
import com.siena.pokedex.R;
import com.siena.pokedex.adapters.PokemonInfoAdapter;
import com.siena.pokedex.models.Pokemon;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siena Aguayo on 12/28/14.
 */
public class PokeInfoFragment extends Fragment {
  @InjectView(R.id.poke_info_listview) ListView listView;

  public PokeInfoFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_poke_info_listview, container, false);
    ButterKnife.inject(this, rootView);
    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    int id = getArguments().getInt(PokemonUtil.POKEMON_ID_KEY);

    DataAdapter mDbHelper = new DataAdapter(getActivity());
    mDbHelper.createDatabase();
    mDbHelper.open();
    String name = mDbHelper.getIdentifierById(id);
    String genus = mDbHelper.getGenusById(id);
    List<Integer> types = mDbHelper.getPokemonTypeData(id);
    ArrayList<String> typeNames = new ArrayList<>();
    for (Integer type : types) {
      typeNames.add(mDbHelper.getTypeById(type));
    }

    Pokemon poke = new Pokemon(id, name);
    poke.setTypes(typeNames);
    poke.setGenus(genus);
    listView.setAdapter(new PokemonInfoAdapter(getActivity(), poke));
    return rootView;
  }
}
