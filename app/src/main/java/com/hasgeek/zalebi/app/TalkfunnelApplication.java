package com.hasgeek.zalebi.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.orm.SugarApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TalkfunnelApplication  extends SugarApp{
    String dbName = "hasgeek_sugar.db";
    String dbPath = "/data/data/com.hasgeek.zalebi/databases/"+dbName;

    @Override
    public final void onCreate() {
        init();
        super.onCreate();
    }

    private void init() {
        initDB();
    }

    private void initDB() {
        try {
            if (!doesDatabaseExist(this, dbName)) {
                Log.d("hasgeek", "DB not found !!!!");
                Context context = getApplicationContext();
                SQLiteDatabase db = context.openOrCreateDatabase(dbName, context.MODE_PRIVATE, null);
                db.close();
                Log.d("hasgeek","Closed db after openOrCreateDB method");
                InputStream dbInput = getApplicationContext().getAssets().open(dbName);
                Log.d("hasgeek","found DB from assets folder ");
                OutputStream dbOutput = new FileOutputStream(dbPath);
                try {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = dbInput.read(buffer)) > 0) {
                        dbOutput.write(buffer, 0, length);
                    }
                    Log.d("hasgeek","Copied initial data to the actual DB");
                }
                finally {
                    dbOutput.flush();
                    dbOutput.close();
                    dbInput.close();
                }
            }
            else{
                Log.d("hasgeek", "DB Exists, so continue ******");
            }
        } catch (Exception e) {
            Log.w("hasgeek","Exception while copying the DB from assets "+e.getMessage());
            e.toString();
        }
    }

    private boolean doesDatabaseExist(ContextWrapper context, String dbName)       {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
