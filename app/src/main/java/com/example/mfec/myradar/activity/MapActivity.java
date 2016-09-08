package com.example.mfec.myradar.activity;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mfec.myradar.R;
import com.example.mfec.myradar.common.MyDBHelper;
import com.example.mfec.myradar.manager.HttpManager;
import com.example.mfec.myradar.model.CollectionPokemon;
import com.example.mfec.myradar.model.PokemonDao;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mMapFragment;
    GoogleMap mMap;
    Marker mMarker;
    double lat = 13.744728, lng = 100.56340;
    double maxLat = lat + 0.02, minLat = lat - 0.02, maxLng = lng + 0.02, minLng = lng - 0.02;
    double moveDistance = 0.0001;
    double latestLat = maxLat, latestLng = minLng;
    LocationManager lm;
    MyDBHelper dbHelper;
    TextView tvTotalPokemon;
    Handler mHandler;
    HandlerThread mBackgroundHandlerThread;
    Handler mBackgroundHandler;
    private final int left = 0, right = 1;
    private int id = 0;
    ArrayList<Marker> markers;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBackgroundHandlerThread.quit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("latitude", latestLat);
        outState.putDouble("longitude", latestLng);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        dbHelper = new MyDBHelper(this);
        markers = new ArrayList<>();
        tvTotalPokemon = (TextView) findViewById(R.id.total_pokemon);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (savedInstanceState != null){
            latestLat = savedInstanceState.getDouble("latitude");
            latestLng = savedInstanceState.getDouble("longitude");
        }

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dbHelper.deleteTimeoutPokemon();
                List<PokemonDao> pokemonList = dbHelper.getPokemonList();
                clearMarker();
                for (PokemonDao pokemon : pokemonList) {
                    int number = Integer.parseInt(pokemon.getNumber());
                    int resId = getResources().getIdentifier("ic_pokemon_no_" + String.valueOf(number), "drawable", getPackageName());
                    Marker mPokemon = mMap.addMarker(new MarkerOptions().position(new LatLng(pokemon.getLatitude(), pokemon.getLongitude())));
                    mPokemon.setTitle(pokemon.getNumber());
                    mPokemon.setSnippet(pokemon.getName());
                    mPokemon.setIcon(BitmapDescriptorFactory.fromResource(resId));
                    markers.add(mPokemon);
                }

                tvTotalPokemon.setText("Total " + pokemonList.size() + " Pokemon");

                Message message = new Message();
                message.arg1 = msg.arg1;
                mBackgroundHandler.sendMessageDelayed(message, 9000);
            }
        };

        mBackgroundHandlerThread = new HandlerThread("BackgroundHandler");
        mBackgroundHandlerThread.start();
        mBackgroundHandler = new Handler(mBackgroundHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Message message = new Message();
                message.arg1 = msg.arg1;

                if (message.arg1 == left) {
                    walkToLeft();
                } else {
                    walkToRight();
                }
            }
        };
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    private void clearMarker() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        locateLatLngLocation();
        //HttpManager.getInstance().getService().loadPokemonList(dbHelper.getUserToken(), lat, lng).enqueue(pokemonCallBack);
    }

    private void locateLatLngLocation() {
        LatLng coordinate = new LatLng(lat, lng);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));
        mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
        mMarker.setTitle("Center");
        mMarker.setSnippet("This is center point");
        mMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        Message message = new Message();
        message.arg1 = right;
        mBackgroundHandler.sendMessageDelayed(message, 1000);
    }

    private void walkToLeft() {
        if (latestLat >= minLat) {
            if (minLng <= latestLng) {
                HttpManager.getInstance().getService().loadPokemonList(dbHelper.getUserToken(), latestLat, latestLng).enqueue(pokemonLeftCallBack);
            } else {
                latestLat -= moveDistance;
                walkToRight();
            }
        } else {
        }
    }

    private void walkToRight() {
        if (latestLat >= minLat) {
            if (maxLng >= latestLng) {
                HttpManager.getInstance().getService().loadPokemonList(dbHelper.getUserToken(), latestLat, latestLng).enqueue(pokemonRightCallBack);
            } else {
                latestLat -= moveDistance;
                walkToLeft();
            }
        } else {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        lm.removeUpdates(locationListener);
    }

    private Callback<CollectionPokemon> pokemonLeftCallBack = new Callback<CollectionPokemon>() {
        @Override
        public void onResponse(Call<CollectionPokemon> call, Response<CollectionPokemon> response) {
            if (response.isSuccessful()) {
                for (PokemonDao pokemon : response.body().getPokemonList()) {
                    dbHelper.addPokemon(pokemon);
                }
                latestLng -= moveDistance;
                
                Message message = new Message();
                message.arg1 = left;
                mBackgroundHandler.sendMessageDelayed(message, 1000);
            } else {
                try {
                    Toast.makeText(MapActivity.this, response.errorBody().string(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<CollectionPokemon> call, Throwable t) {
            Toast.makeText(MapActivity.this, t.toString(), Toast.LENGTH_LONG).show();
        }
    };

    private Callback<CollectionPokemon> pokemonRightCallBack = new Callback<CollectionPokemon>() {
        @Override
        public void onResponse(Call<CollectionPokemon> call, Response<CollectionPokemon> response) {
            if (response.isSuccessful()) {
                for (PokemonDao pokemon : response.body().getPokemonList()) {
                    dbHelper.addPokemon(pokemon);
                }
                latestLng += moveDistance;

                Message message = new Message();
                message.arg1 = right;
                mHandler.sendMessage(message);
            } else {
                try {
                    Toast.makeText(MapActivity.this, response.errorBody().string(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<CollectionPokemon> call, Throwable t) {
            Toast.makeText(MapActivity.this, t.toString(), Toast.LENGTH_LONG).show();
        }
    };
}
