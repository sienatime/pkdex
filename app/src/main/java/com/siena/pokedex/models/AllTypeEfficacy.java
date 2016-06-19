package com.siena.pokedex.models;

import android.util.Log;
import com.siena.pokedex.models.persisted.PokemonType;
import com.siena.pokedex.models.persisted.TypeEfficacy;
import io.realm.Realm;
import io.realm.RealmList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Siena Aguayo on 1/2/15.
 */
public class AllTypeEfficacy {
  private RealmList<PokemonType> weakTo;
  private RealmList<PokemonType> damagedNormallyBy;
  private RealmList<PokemonType> resistantTo;
  private RealmList<PokemonType> immuneTo;
  
  public AllTypeEfficacy(RealmList<PokemonType> weakTo, RealmList<PokemonType> damagedNormallyBy,
      RealmList<PokemonType> resistantTo, RealmList<PokemonType> immuneTo) {
    this.weakTo = weakTo;
    this.damagedNormallyBy = damagedNormallyBy;
    this.resistantTo = resistantTo;
    this.immuneTo = immuneTo;
  }
  
  public RealmList<PokemonType> getWeakTo() {
    return weakTo;
  }
  
  public RealmList<PokemonType> getDamagedNormallyBy() {
    return damagedNormallyBy;
  }
  
  public RealmList<PokemonType> getResistantTo() {
    return resistantTo;
  }
  
  public RealmList<PokemonType> getImmuneTo() {
    return immuneTo;
  }

  public static AllTypeEfficacy createAllTypeEfficacy(RealmList<PokemonType> types) {
    Realm realm = Realm.getDefaultInstance();
    List<List<TypeEfficacy>> allTypeEfficaciesList = new ArrayList<>();

    for (PokemonType type : types) {
      allTypeEfficaciesList.add(
          realm.where(TypeEfficacy.class).equalTo("targetTypeId", type.getTypeId()).findAll());
    }

    List<TypeEfficacy> finalEfficacy;

    if (allTypeEfficaciesList.size() > 1) {
      finalEfficacy = consolidateTypeEfficacy(allTypeEfficaciesList);
    } else {
      finalEfficacy = allTypeEfficaciesList.get(0);
    }

    RealmList<PokemonType> weakTo = new RealmList<>();
    RealmList<PokemonType> immuneTo = new RealmList<>();
    RealmList<PokemonType> resistantTo = new RealmList<>();
    RealmList<PokemonType> damagedNormallyBy = new RealmList<>();

    for (TypeEfficacy efficacy : finalEfficacy) {
      PokemonType damageType = new PokemonType(efficacy.getDamageTypeId());
      switch (efficacy.getDamageFactor()) {
        case 0:
          immuneTo.add(damageType);
          break;
        case 25:
          resistantTo.add(damageType);
          break;
        case 50:
          resistantTo.add(damageType);
          break;
        case 100:
          damagedNormallyBy.add(damageType);
          break;
        case 200:
          weakTo.add(damageType);
          break;
        case 400:
          weakTo.add(damageType);
          break;
        default:
          Log.v("type efficacy",
              "unknown damage factor: " + Integer.toString(efficacy.getDamageFactor()));
      }
    }

    return new AllTypeEfficacy(weakTo, damagedNormallyBy, resistantTo, immuneTo);
  }

  private static List<TypeEfficacy> consolidateTypeEfficacy(
      List<List<TypeEfficacy>> allTypeEfficaciesList) {
    assert (allTypeEfficaciesList.get(0).size() == allTypeEfficaciesList.get(1).size());

    List<TypeEfficacy> finalEfficacy = new ArrayList<>();

    for (int i = 0; i < allTypeEfficaciesList.get(0).size(); i++) {
      TypeEfficacy damageType1 = allTypeEfficaciesList.get(0).get(i);
      TypeEfficacy damageType2 = allTypeEfficaciesList.get(1).get(i);

      assert (damageType1.getDamageTypeId() == damageType2.getDamageTypeId());
      int combinedDamage = damageType1.getDamageFactor() * damageType2.getDamageFactor() / 100;
      TypeEfficacy combinedTypeEfficacy =
          new TypeEfficacy(damageType1.getDamageTypeId(), combinedDamage);
      finalEfficacy.add(combinedTypeEfficacy);
    }

    return finalEfficacy;
  }
}
