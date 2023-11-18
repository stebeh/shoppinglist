package org.openintents.shopping.provider;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.File;
import java.io.IOException;

import org.openintents.shopping.ui.PreferenceActivity;


public class DatabaseContext extends ContextWrapper {
    private static String TAG = "DatabaseContext";

    public DatabaseContext(Context context) {
        super(context);
    }

    @Override
    public File getDatabasePath(String name)  {
        // Parameter name="shopping.db" as passed as DB_NAME to super constructor in ShoppingDatabase constructor
        SharedPreferences sp = getSharedPreferences(
                "org.openintents.shopping_preferences", MODE_PRIVATE);
        final Boolean externalDbUsed = sp.getBoolean(
                PreferenceActivity.PREFS_EXTERNALDB_USED,
                PreferenceActivity.PREFS_EXTERNALDB_USED_DEFAULT);
        final String dirPath = sp.getString(
                PreferenceActivity.PREFS_EXTERNALDB_PATH,
                PreferenceActivity.PREFS_EXTERNALDB_PATH_DEFAULT);
        if (externalDbUsed &&
                !dirPath.equalsIgnoreCase(PreferenceActivity.PREFS_EXTERNALDB_PATH_DEFAULT)) {
            String path = null;
            File parentFile=new File(dirPath);
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }
            String parentPath=parentFile.getAbsolutePath();
            if (parentPath.lastIndexOf("\\/")!=-1){
                path = dirPath + name;
            } else {
                path = dirPath + File.separator + name;
            }
            File dbFile = new File(path);
            try {
                if(!dbFile.exists()){
                    dbFile.createNewFile();
                }
            } catch (IOException e) {
                Log.e(TAG,"getDatabasePath", e);
            }
            return dbFile;
        } else {
            return super.getDatabasePath(name);
        }
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), factory, errorHandler);
    }
}
