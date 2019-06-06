package org.shaivam.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import org.shaivam.model.Thirumurai;

import java.sql.SQLException;
import java.util.List;

public class RepoThirumurais {
	
	Dao<Thirumurai, String> thirumuraiDao;
	
	public RepoThirumurais(DatabaseHelper db)
	{
		try {
			thirumuraiDao = db.getThirumuraiDao();
		} catch (SQLException e) {
			// TODO: Exception Handling
			e.printStackTrace();
		}
	}
	
	public int create(Thirumurai thirumurai)
	{
		try {
			return thirumuraiDao.create(thirumurai);
		} catch (SQLException e) {
			// TODO: Exception Handling
			e.printStackTrace();
		}
		return 0;
	}
	public int update(Thirumurai thirumurai)
	{
		try {
			return thirumuraiDao.update(thirumurai);
		} catch (SQLException e) {
			// TODO: Exception Handling
			e.printStackTrace();
		}
		return 0;
	}
	public int delete(Thirumurai thirumurai)
	{
		try {
			return thirumuraiDao.delete(thirumurai);
		} catch (SQLException e) {
			// TODO: Exception Handling
			e.printStackTrace();
		}
		return 0;
	}
	public Thirumurai getByThirumurainame(String thirumurainame)
	{		
		try {
			QueryBuilder<Thirumurai, String> qb = thirumuraiDao.queryBuilder();
			
			qb.where().eq("thirumurainame", thirumurainame);
			
			PreparedQuery<Thirumurai> pq = qb.prepare();
			return thirumuraiDao.queryForFirst(pq);
		} catch (SQLException e) {
			// TODO: Exception Handling
			e.printStackTrace();
		}
		return null;
	}
	public Thirumurai getById(String id)
	{
		try {
			QueryBuilder<Thirumurai, String> qb = thirumuraiDao.queryBuilder();

			qb.where().eq("id", id);

			PreparedQuery<Thirumurai> pq = qb.prepare();
			return thirumuraiDao.queryForFirst(pq);
		} catch (SQLException e) {
			// TODO: Exception Handling
			e.printStackTrace();
		}
		return null;
	}
	public List<Thirumurai> getAll()
	{		
		try {
			return thirumuraiDao.queryForAll();
		} catch (SQLException e) {
			// TODO: Exception Handling
			e.printStackTrace();
		}
		return null;
	}

}
