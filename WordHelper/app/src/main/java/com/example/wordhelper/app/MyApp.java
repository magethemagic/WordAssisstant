package com.example.wordhelper.app;

import android.app.Application;

import com.example.wordhelper.dao.DaoMaster;
import com.example.wordhelper.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

public class MyApp extends Application {
    private DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"word_db");
        Database database =helper.getReadableDb();
        daoSession = new DaoMaster(database).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
