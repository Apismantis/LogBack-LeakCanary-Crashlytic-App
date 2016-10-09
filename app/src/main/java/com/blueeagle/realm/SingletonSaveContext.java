package com.blueeagle.realm;

import android.content.Context;

public class SingletonSaveContext {

    private Context context;
    private static SingletonSaveContext instance;

    public Context getContext() {
        return context;
    }

    public void setContext(Context mContext)
    {
        context = mContext;
    }

    public static SingletonSaveContext getInstance() {
        if(instance == null)
            instance = new SingletonSaveContext();

        return instance;
    }
}
