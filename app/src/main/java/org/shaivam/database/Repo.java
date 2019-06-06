package org.shaivam.database;
import android.content.Context;

public class Repo {
	
	DatabaseHelper db;
	
	public RepoThirumurais repoThirumurais;
	public RepoThirumurai_songs repoThirumurai_songs;

	public Repo(Context context)
	{
		DatabaseManager<DatabaseHelper> manager = new DatabaseManager<DatabaseHelper>();
		db = manager.getHelper(context);
		
		repoThirumurais = new RepoThirumurais(db);
		repoThirumurai_songs = new RepoThirumurai_songs(db);
	}	
	
}
