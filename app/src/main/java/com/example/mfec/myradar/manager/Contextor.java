package com.example.mfec.myradar.manager;

import android.content.Context;

/**
 * Created by MFEC on 1/20/2016.
 */
public class Contextor {
    private static Contextor instance;

    public static Contextor getInstance(){
        if(instance == null){
            instance = new Contextor();
        }
        return instance;
    }

    private Context mContext;

    public void init(Context context){
        mContext = context;
    }

    public Context getContext(){
        return mContext;
    }

    private Contextor(){

    }
}
