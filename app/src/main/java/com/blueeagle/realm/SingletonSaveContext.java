package com.blueeagle.realm;

import android.content.Context;

public class SingletonSaveContext {

    private static SingletonSaveContext instance;
    private Context context;

    public static SingletonSaveContext getInstance() {
        if (instance == null)
            instance = new SingletonSaveContext();

        return instance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context mContext) {
        context = mContext;
    }
}
