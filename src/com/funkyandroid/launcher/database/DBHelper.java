package com.funkyandroid.launcher.database;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database Helper to create the tables required to support the launcher.
 */
public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    static final String DB_NAME = "funky_launcher";
    static final int DB_VERSION = 2;
    public static final String CATEGORIES_TABLE = "categories";
    public static final String APPS_TABLE = "apps";
    public static final String FAVORITES_TABLE = "favourites";

    public DBHelper(final Context context) { //
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CATEGORIES_TABLE + " (_id INTEGER PRIMARY KEY autoincrement, name TEXT)");
        db.execSQL("create table " + APPS_TABLE + " (packageName TEXT, category_id INT)");
        db.execSQL("insert into " + CATEGORIES_TABLE + "(name) values ('Apps')");
        db.execSQL("create table " + FAVORITES_TABLE + " (package TEXT PRIMARY KEY, bar INT)");

        int catId = -1;
        String[] idCols = {"_id"};
        Cursor cur = db.query(CATEGORIES_TABLE, idCols, "name = 'Apps'", null, null, null, null, null );
        try {
            if(cur.getCount() == 0) {
                return;
            }

            while(cur.moveToNext()) {
                catId = cur.getInt(0);
            }
        } finally {
            cur.close();
        }

        PackageManager pm = context.getPackageManager();
        Intent appStartIntent = new Intent(Intent.ACTION_MAIN);
        appStartIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        for(ResolveInfo packageInfo : pm.queryIntentActivities(appStartIntent, 0)) {
            db.execSQL("insert into "+APPS_TABLE+"(packageName, category_id) values ('"+packageInfo.activityInfo.packageName+"', "+catId+")");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 2) {
            db.execSQL("create table " + FAVORITES_TABLE + " (package TEXT PRIMARY KEY, bar INT)");
        }
    }
}
