package com.example.mfec.myradar.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mfec.myradar.R;
import com.example.mfec.myradar.common.MyDBHelper;
import com.example.mfec.myradar.manager.HttpManager;
import com.example.mfec.myradar.model.LoginDao;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button loginBtn;
    private MyDBHelper dbHelper;
    private final int MY_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }*/
        initInstances();
    }

    private void initInstances() {
        dbHelper = new MyDBHelper(this);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login_button);

        loginBtn.setOnClickListener(loginBtnListener);
    }

    private View.OnClickListener loginBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkEmailAndPassword()) {
                login();
            }
        }
    };

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST);
        }
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        HttpManager.getInstance().getService().loginWithEmail(email, password).enqueue(loginCallback);
    }

    public boolean checkEmailAndPassword() {
        if (etEmail.getText().toString().trim().length() > 0 &&
                etPassword.getText().toString().trim().length() > 0 &&
                isEmailValid()) {
            return true;
        } else {
            Toast.makeText(this, "Please check your email and password.", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean isEmailValid() {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = etEmail.getText().toString().trim();
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                login();
            } else {
                finish();
            }
        }
    }

    private Callback<LoginDao> loginCallback = new Callback<LoginDao>() {
        @Override
        public void onResponse(Call<LoginDao> call, Response<LoginDao> response) {
            if (response.isSuccessful()) {
                Log.d("login", "success");
                dbHelper.addUserToken(response.body().getToken());
                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
            } else {
                try {
                    Toast.makeText(LoginActivity.this, response.errorBody().string(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<LoginDao> call, Throwable t) {
            Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_LONG).show();
        }
    };
}
