package com.montek.tohafaa;
import android.content.Context;
import android.support.multidex.*;
public class MyApplication extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
     //   Realm.init(this); //initialize other plugins
    }
}