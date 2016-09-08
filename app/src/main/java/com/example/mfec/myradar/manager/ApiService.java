package com.example.mfec.myradar.manager;

import com.example.mfec.myradar.model.CollectionPokemon;
import com.example.mfec.myradar.model.LoginDao;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by MFEC on 9/7/2016.
 */
public interface ApiService {

    @GET("pokemon")
    Call<Void> checkServerStatus();

    @FormUrlEncoded
    @POST("pokemon/login")
    Call<LoginDao> loginWithEmail(@Field("email") String email, @Field("password") String password);

    @GET("pokemon/catchable")
    Call<CollectionPokemon> loadPokemonList(@Header("pokemon_token") String token,
                                            @Query("latitude") double latitude,
                                            @Query("longitude") double longitude);
}
