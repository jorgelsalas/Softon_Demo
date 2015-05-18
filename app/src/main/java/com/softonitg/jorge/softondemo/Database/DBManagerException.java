package com.softonitg.jorge.softondemo.Database;

/**
 * Created by Jorge on 15/05/2015.
 * Classifies different DB exceptions thrown
 */
public class DBManagerException extends Exception{

    private static final long serialVersionUID = -6570023520371955615L;
    public static final int USER_NOT_UNIQUE = 1;
    public static final String USER_NOT_UNIQUE_TEXT = "More than 1 user has the same username in the database";
    public static final int USER_NOT_FOUND = 2;
    public static final String USER_NOT_FOUND_TEXT = "Username not found in the database";

    public String description;

    public DBManagerException() {

    }

    public DBManagerException(int exceptionType) {
        switch (exceptionType){
            case USER_NOT_UNIQUE:
                description = USER_NOT_UNIQUE_TEXT;
                break;

            case USER_NOT_FOUND:
                description = USER_NOT_FOUND_TEXT;
                break;
        }
    }

    public String getDescription() {
        return description;
    }
}
