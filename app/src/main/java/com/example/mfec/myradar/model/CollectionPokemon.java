package com.example.mfec.myradar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MFEC on 9/7/2016.
 */
public class CollectionPokemon implements Parcelable {
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<PokemonDao> pokemonList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PokemonDao> getPokemonList() {
        return pokemonList;
    }

    public void setPokemonList(List<PokemonDao> pokemonList) {
        this.pokemonList = pokemonList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeTypedList(this.pokemonList);
    }

    public CollectionPokemon() {
    }

    protected CollectionPokemon(Parcel in) {
        this.message = in.readString();
        this.pokemonList = in.createTypedArrayList(PokemonDao.CREATOR);
    }

    public static final Creator<CollectionPokemon> CREATOR = new Creator<CollectionPokemon>() {
        @Override
        public CollectionPokemon createFromParcel(Parcel source) {
            return new CollectionPokemon(source);
        }

        @Override
        public CollectionPokemon[] newArray(int size) {
            return new CollectionPokemon[size];
        }
    };
}
