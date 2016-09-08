package com.example.mfec.myradar.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mfec.myradar.model.PokemonDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MFEC on 9/7/2016.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MyRadar";
    private static final int DB_VERSION = 2;

    private final String TABLE_USER = "user";
    private final String USER_TOKEN = "token";

    private final String TABLE_POKEMON = "pokemon";
    private final String POKEMON_EXP = "expirationTimestamp";
    private final String POKEMON_LAT = "latitude";
    private final String POKEMON_LNG = "longitude";
    private final String POKEMON_NAME = "name";
    private final String POKEMON_NUMBER = "number";
    private final String POKEMON_ID = "id";

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USER + " (" + USER_TOKEN + " TEXT);");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_POKEMON + " (" + POKEMON_EXP + " TEXT, "
                + POKEMON_LAT + " REAL, " + POKEMON_LNG + " REAL, " + POKEMON_NAME + " TEXT, "
                + POKEMON_NUMBER + " TEXT, " + POKEMON_ID + " TEXT UNIQUE"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_POKEMON);
        onCreate(sqLiteDatabase);
    }

    public void addUserToken(String token) {
        if (userTokenExist()) {
            updateUserToken(token);
        } else {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("INSERT INTO " + TABLE_USER + " (" + USER_TOKEN + ") VALUES ('" + token + "');");
            db.close();
        }
    }

    public void addPokemon(PokemonDao pokemon) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT OR REPLACE INTO " + TABLE_POKEMON + " (" + POKEMON_EXP + ","
                + POKEMON_LAT + "," + POKEMON_LNG + "," + POKEMON_NAME + ","
                + POKEMON_NUMBER + "," + POKEMON_ID + ") VALUES (" + pokemon.getExpirationTimestamp() + ","
                + pokemon.getLatitude() + "," + pokemon.getLongitude() + "," + "'" + pokemon.getName() + "'" + ","
                + "'" + pokemon.getNumber() + "'" + "," + "'" + pokemon.getId() + "'" + ");");
        db.close();
    }

    public List<PokemonDao> getPokemonList() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_POKEMON, null);
        cursor.moveToFirst();
        List<PokemonDao> pokemonList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            PokemonDao pokemonDao = new PokemonDao();
            pokemonDao.setExpirationTimestamp(cursor.getLong(cursor.getColumnIndex(POKEMON_EXP)));
            pokemonDao.setLatitude(cursor.getDouble(cursor.getColumnIndex(POKEMON_LAT)));
            pokemonDao.setLongitude(cursor.getDouble(cursor.getColumnIndex(POKEMON_LNG)));
            pokemonDao.setName(cursor.getString(cursor.getColumnIndex(POKEMON_NAME)));
            pokemonDao.setNumber(cursor.getString(cursor.getColumnIndex(POKEMON_NUMBER)));
            pokemonDao.setId(cursor.getString(cursor.getColumnIndex(POKEMON_ID)));
            pokemonList.add(pokemonDao);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return pokemonList;
    }

    public void deleteTimeoutPokemon() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_POKEMON + " WHERE " + POKEMON_EXP + " < " + System.currentTimeMillis() + ";");
        db.close();
    }

    private void updateUserToken(String token) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_USER + " SET " + USER_TOKEN + " = '" + token + "';");
        db.close();
    }

    public String getUserToken() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
        cursor.moveToFirst();
        String token = cursor.getString(cursor.getColumnIndex(USER_TOKEN));
        cursor.close();
        db.close();
        return token;
    }

    public boolean userTokenExist() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
}
