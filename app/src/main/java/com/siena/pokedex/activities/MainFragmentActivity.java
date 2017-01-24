package com.siena.pokedex.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.siena.pokedex.R;
import com.siena.pokedex.database.realm.PopulateRealm;
import com.siena.pokedex.database.sqlite.DataAdapter;
import com.siena.pokedex.fragments.PokemonIndexFragment;
import com.siena.pokedex.models.persisted.Pokemon;
import io.realm.Realm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainFragmentActivity extends ActionBarActivity {
  @BindView(R.id.toolbar) Toolbar toolbar;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_fragment);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    final Context context = this;

    //if (true) {
    //  copyBundledRealmFile(this.getResources().openRawResource(R.raw.pokedex), "pokedex");
    //}

    Realm.init(this);
    Realm realm = Realm.getDefaultInstance();
    Pokemon bulbasaur = realm.where(Pokemon.class).equalTo("id", 1).findFirst();

    if (bulbasaur == null) {
      new AsyncTask<Context, Integer, Long>() {
        protected Long doInBackground(Context... aParams) {
          publishProgress(1);
          Realm asyncRealm = Realm.getDefaultInstance();
          DataAdapter dataAdapter = new DataAdapter(context);
          publishProgress(5);

          asyncRealm.beginTransaction();
          PopulateRealm.addPokemonData(asyncRealm, dataAdapter);
          publishProgress(10);
          PopulateRealm.addTypeData(asyncRealm, dataAdapter);
          publishProgress(20);
          PopulateRealm.addTypeEfficacy(asyncRealm, dataAdapter);
          publishProgress(30);
          PopulateRealm.addEncounters(asyncRealm, dataAdapter);
          publishProgress(70);
          PopulateRealm.addEncounterConditionValueId(asyncRealm, dataAdapter);
          publishProgress(80);
          PopulateRealm.consolidateAllEncounters(asyncRealm, dataAdapter);
          publishProgress(90);
          asyncRealm.commitTransaction();

          asyncRealm.close();
          dataAdapter.close();
          return 100L;
        }

        protected void onProgressUpdate(Integer... progress) {
          Log.i("PopulateRealm", String.format("%s%%", progress));
        }

        protected void onPostExecute(Long aResult) {
          Log.i("PopulateRealm", "Done!");
          Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show();
          instantiateListFragment(savedInstanceState);
        }
      }.execute();
    } else {
      instantiateListFragment(savedInstanceState);
    }
    realm.close();
  }

  private void instantiateListFragment(Bundle savedInstanceState) {
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().add(R.id.container, new PokemonIndexFragment()).commit();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      return true;
    } else if (id == android.R.id.home) {
      getSupportFragmentManager().popBackStack();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    getFragmentManager().popBackStack();
  }

  private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
    try {
      File file = new File(this.getFilesDir(), outFileName);
      FileOutputStream outputStream = new FileOutputStream(file);
      byte[] buf = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buf)) > 0) {
        outputStream.write(buf, 0, bytesRead);
      }
      outputStream.close();
      return file.getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
