package com.softonitg.jorge.softondemo.Database;

/**
 * Created by Jorge on 15/05/2015.
 * Constants related to DB operations
 */
public class DBConstants {

    public static final String DB_NAME = "softon_demo_db";
    public static final Integer VERSION = 1;

    public static final String USER_TABLE_NAME = "Users";
    public static final String USER_ID = "id_users";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SALT = "salt";

    public static final String CREATE_USER_TABLE_CMD =

            "CREATE TABLE " + USER_TABLE_NAME + " ("
                    + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + USERNAME + " TEXT UNIQUE NOT NULL, "
                    + PASSWORD + " TEXT NOT NULL, "
                    + SALT + " TEXT NOT NULL)";

    final static String[] tables = {USER_TABLE_NAME};
}
