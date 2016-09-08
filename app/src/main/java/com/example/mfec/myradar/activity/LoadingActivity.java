package com.example.mfec.myradar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.example.mfec.myradar.R;
import com.example.mfec.myradar.common.MyDBHelper;
import com.example.mfec.myradar.manager.HttpManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {

    long time = 2000L;
    long delay_time;
    Handler handler;
    Runnable runnable;
    MyDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);
        initInstances();
    }

    private void initInstances() {
        dbHelper = new MyDBHelper(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                HttpManager.getInstance().getService().checkServerStatus().enqueue(svCallback);
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    private Callback<Void> svCallback = new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.isSuccessful()) {
                if (dbHelper.userTokenExist()) {
                    skipLoginToMap();
                    Log.d("token", dbHelper.getUserToken());
                } else {
                    forwardToLogin();
                }
            } else {
                showAlertDialog();
                Log.d("loading unsuccess", response.errorBody().toString());
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Log.d("loading failure", t.toString());
        }
    };

    private void skipLoginToMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        finish();
    }

    private void forwardToLogin() {
        Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Server is unavailable")
                .setMessage("Please try again later")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }
}
