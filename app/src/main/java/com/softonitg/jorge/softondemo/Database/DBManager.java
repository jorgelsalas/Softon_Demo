package com.softonitg.jorge.softondemo.Database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.softonitg.jorge.softondemo.Crypto.HashUtils;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Jorge on 15/05/2015.
 * Supplies an interface to interact with underlying database
 */
public class DBManager {

    private SQLiteDatabase mDB = null;
    private DBHelper mDbHelper;
    private final static String TAG = "DB MANAGER LOG TAG";

    public DBManager(Context context){
        mDbHelper = new DBHelper(context);
        mDB = mDbHelper.getWritableDatabase();
    }

    // Delete all records
    public void clearAll() {
        for (String table : DBConstants.tables){
            mDB.delete(table, null, null);
        }
    }

    public void close(){
        mDB.close();
    }

    public void deleteDatabase(){
        mDbHelper.deleteDatabase();
    }

    public boolean verifyUserCredentials(String user, String password) throws NoSuchAlgorithmException, DBManagerException {
        String hashedPass, dbPassword, salt;
        boolean valid = false;
        Cursor c = mDB.query(
                DBConstants.USER_TABLE_NAME,
                new String [] {DBConstants.PASSWORD, DBConstants.SALT},
                DBConstants.USERNAME + "=?",
                new String[] {user},
                null,
                null,
                null);

        if (c.getCount() == 0){
            DBManagerException dbme = new DBManagerException(DBManagerException.USER_NOT_FOUND);
            Log.e(TAG, dbme.getDescription());
            throw dbme;
        }
        else if (c.getCount() > 1){
            DBManagerException dbme = new DBManagerException(DBManagerException.USER_NOT_UNIQUE);
            Log.e(TAG,dbme.getDescription());
            throw dbme;
        }

        c.moveToNext();
        dbPassword = c.getString(0);
        salt = c.getString(1);
        c.close();
        hashedPass = HashUtils.encrypt(password, salt);
        valid = hashedPass.equalsIgnoreCase(dbPassword);
        return valid;
    }

}
