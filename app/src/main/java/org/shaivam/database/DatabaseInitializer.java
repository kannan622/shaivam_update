package org.shaivam.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.PrefManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseInitializer extends SQLiteOpenHelper {

    private static String DB_NAME = "thriumurai.sqlite";
    private final Context context;
    private SQLiteDatabase database;

    public DatabaseInitializer(Context context) {
        super(context, DB_NAME, null, MyApplication.prefManager.getInt(PrefManager.DATABASE_VERSION,
                AppConfig.DATABASE_VERSION));
        this.context = context;
    }

    public void createDatabase() throws IOException {

        this.getReadableDatabase();
        try {
            copyDatabase();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    private void copyDatabase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);

        String outFileName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            SQLiteDatabase database = this.getReadableDatabase();
            outFileName = database.getPath();
        } else {
            outFileName = context.getDatabasePath(DB_NAME).toString();
        }


        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
