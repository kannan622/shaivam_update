package org.shaivam.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.shaivam.model.Thirumurai;
import org.shaivam.model.Thirumurai_songs;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.MyApplication;
import org.shaivam.utils.PrefManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

  private static final String DATABASE_NAME = "thriumurai.sqlite";

  private Dao<Thirumurai, String> thirumuraiDao = null;
  private Dao<Thirumurai_songs, String> thirumurai_songsDao = null;
  Context context;

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, MyApplication.prefManager.getInt(PrefManager
        .DATABASE_VERSION, AppConfig.DATABASE_VERSION));

    this.context = context;
    DatabaseInitializer initializer = new DatabaseInitializer(context);
    try {
      initializer.createDatabase();
      initializer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  @Override
  public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
  }

  public Dao<Thirumurai, String> getThirumuraiDao() throws SQLException {
    if (thirumuraiDao == null) {
      thirumuraiDao = getDao(Thirumurai.class);
    }
    return thirumuraiDao;
  }

  public Dao<Thirumurai_songs, String> getThirumurai_songsDao() throws SQLException {
    if (thirumurai_songsDao == null) {
      thirumurai_songsDao = getDao(Thirumurai_songs.class);
    }
    return thirumurai_songsDao;
  }


  @Override
  public void close() {
    super.close();
    thirumuraiDao = null;
    thirumurai_songsDao = null;
  }
}
