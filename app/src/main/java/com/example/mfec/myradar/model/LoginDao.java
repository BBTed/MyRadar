package com.example.mfec.myradar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MFEC on 9/7/2016.
 */
public class LoginDao implements Parcelable{

    @SerializedName("message")
    private String message;
    @SerializedName("token")
    private String token;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeString(this.token);
    }

    public LoginDao() {
    }

    protected LoginDao(Parcel in) {
        this.message = in.readString();
        this.token = in.readString();
    }

    public static final Creator<LoginDao> CREATOR = new Creator<LoginDao>() {
        @Override
        public LoginDao createFromParcel(Parcel source) {
            return new LoginDao(source);
        }

        @Override
        public LoginDao[] newArray(int size) {
            return new LoginDao[size];
        }
    };
}
