package com.example.mfec.myradar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MFEC on 9/7/2016.
 */
public class PokemonDao implements Parcelable {

    @SerializedName("expiration_timestamp")
    private long expirationTimestamp;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("name")
    private String name;
    @SerializedName("number")
    private String number;
    @SerializedName("id")
    private String id;

    public long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.expirationTimestamp);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.name);
        dest.writeString(this.number);
        dest.writeString(this.id);
    }

    public PokemonDao() {
    }

    protected PokemonDao(Parcel in) {
        this.expirationTimestamp = in.readLong();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.name = in.readString();
        this.number = in.readString();
        this.id = in.readString();
    }

    public static final Creator<PokemonDao> CREATOR = new Creator<PokemonDao>() {
        @Override
        public PokemonDao createFromParcel(Parcel source) {
            return new PokemonDao(source);
        }

        @Override
        public PokemonDao[] newArray(int size) {
            return new PokemonDao[size];
        }
    };
}
