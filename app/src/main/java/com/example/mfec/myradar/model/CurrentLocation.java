package com.example.mfec.myradar.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MFEC on 9/8/2016.
 */
public class CurrentLocation implements Parcelable {

    private double latitude;
    private double longitude;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    public CurrentLocation(double latestLat, double latestLng) {
        setLatitude(latestLat);
        setLongitude(latestLng);
    }

    protected CurrentLocation(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<CurrentLocation> CREATOR = new Creator<CurrentLocation>() {
        @Override
        public CurrentLocation createFromParcel(Parcel source) {
            return new CurrentLocation(source);
        }

        @Override
        public CurrentLocation[] newArray(int size) {
            return new CurrentLocation[size];
        }
    };
}
