package com.softonitg.jorge.softondemo.Database;

import android.content.Context;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.softonitg.jorge.softondemo.Crypto.HashUtils;

import java.security.NoSuchAlgorithmException;

/**
 * Created by Jorge on 15/05/2015.
 * Helper class required to instantiate DB
 */
public class DBHelper extends SQLiteOpenHelper{

    final private Context mContext;
    private final String defaultUser = "maka";
    private final String defaultPass = "5555";

    public DBHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstants.CREATE_USER_TABLE_CMD);
        try {
            addDefaultUser(db);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addDefaultUser(SQLiteDatabase db) throws NoSuchAlgorithmException {
        String salt = HashUtils.generateKey().getEncoded().toString();
        String hashedPass = HashUtils.encrypt(defaultPass, salt);
        ContentValues values = new ContentValues();
        values.put(DBConstants.USERNAME, defaultUser);
        values.put(DBConstants.PASSWORD, hashedPass);
        values.put(DBConstants.SALT, salt);
        db.insert(DBConstants.USER_TABLE_NAME, null, values);
        Log.w("DBHELPER TAG", "Adding user upon DB CREATION");
    }

    public void deleteDatabase() {
        mContext.deleteDatabase(DBConstants.DB_NAME);
    }
}
