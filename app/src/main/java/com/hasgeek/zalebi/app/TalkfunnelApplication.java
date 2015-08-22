package com.hasgeek.zalebi.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.orm.SugarApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TalkfunnelApplication  extends SugarApp{
    private String dbName = "hasgeek_sugar.db";
    private String dbPath = "/data/data/com.hasgeek.zalebi/databases/"+dbName;
    // The authority for the sync adapter's content provider
    private static final String AUTHORITY = "com.hasgeek.zalebi.sync.provider";
    // An account type, in the form of a domain name
    private static final String ACCOUNT_TYPE = "com.hasgeek.zalebi.account";
    // The account name
    private static final String ACCOUNT = "hasgeek";

    // Sync interval constants
    public static final long SYNC_INTERVAL = 900L;
    private Account mAccount;


    @Override
    public final void onCreate() {
        init();
        super.onCreate();
    }

    private void init() {
        initDB();
        initSync();
    }

    private void initSync() {
        mAccount = createSyncAccount(this);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        ContentResolver.setIsSyncable(mAccount, AUTHORITY, 1);
        ContentResolver.addPeriodicSync(
                mAccount,
                AUTHORITY,
                Bundle.EMPTY,
                SYNC_INTERVAL);
        Log.d("hasgeek", "initiated periodic sync for SyncAdapter");

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


    /** Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account createSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        Context.ACCOUNT_SERVICE);
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(newAccount,null,null);
        return newAccount;
    }

}
