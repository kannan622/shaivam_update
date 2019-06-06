package org.shaivam.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import org.shaivam.model.Thirumurai_songs;

import java.sql.SQLException;
import java.util.List;

public class RepoThirumurai_songs {

    Dao<Thirumurai_songs, String> thirumurai_songsDao;

    public RepoThirumurai_songs(DatabaseHelper db) {
        try {
            thirumurai_songsDao = db.getThirumurai_songsDao();
           /* try {
                thirumurai_songsDao.executeRaw("ALTER TABLE `thirumurai_songs` ADD COLUMN favorites BOOLEAN;");
            } catch (Exception e) {
            }*/

        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
    }

    public int create(Thirumurai_songs thirumurai_songs) {
        try {
            return thirumurai_songsDao.create(thirumurai_songs);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int update(Thirumurai_songs thirumurai_songs) {
        try {
            return thirumurai_songsDao.update(thirumurai_songs);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(Thirumurai_songs thirumurai_songs) {
        try {
            return thirumurai_songsDao.delete(thirumurai_songs);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public Thirumurai_songs getByThirumurai_songsname(String thirumurai_songsname) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();

            qb.where().eq("thirumurai_songsname", thirumurai_songsname);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.queryForFirst(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getByThirumurai_Id(Integer thirumuraiId) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("title").orderBy("title",true);;
            qb.where().eq("thirumuraiId", thirumuraiId);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getSongsBy_Country(String country) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("title").orderBy("title",true);;
            qb.where().lt("thirumuraiId", 8);
            qb.where().eq("country", country);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getSongsBy_Thalam(String thalam) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("title").orderBy("title",true);;
            qb.where().lt("thirumuraiId", 8);
            qb.where().eq("thalam", thalam);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getSongsBy_Author(String author) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("title").orderBy("title",true);;
            qb.where().eq("author", author);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getSongsByTitle(String title) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.where().eq("title", title);
            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getByThirumurai_Id_And_Pan(Integer thirumuraiId, String pann) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
          /*  qb.distinct().selectColumns("title");
            qb.where().eq("pann", pann).and().eq("thirumuraiId", thirumuraiId);*/
            qb.distinct().selectColumns("title").orderBy("title",true);
            qb.where().eq("thirumuraiId", thirumuraiId).and().eq("pann", pann);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getByThirumurai_Id_And_Thalam(Integer thirumuraiId, String thalam) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("title").orderBy("title",true);;
            qb.where().eq("thalam", thalam).and().eq("thirumuraiId", thirumuraiId);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
    public List<Thirumurai_songs> getCountryByThirumurai_Id(Integer thirumuraiId) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("country").selectColumns("thirumuraiId").orderBy("country",true);;
            qb.where().eq("thirumuraiId", thirumuraiId);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getCountry(int thirumuraiId) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("country").orderBy("country",true);
            qb.where().le("thirumuraiId", thirumuraiId);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getByThirumurai_Id_And_Country(Integer thirumuraiId, String country) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("title").orderBy("title",true);;
            qb.where().eq("country", country).and().eq("thirumuraiId", thirumuraiId);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getThalamByThirumurai_Id(Integer thirumuraiId) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("thalam").selectColumns("thirumuraiId");
            qb.where().eq("thirumuraiId", thirumuraiId);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getThalam(int thirumuraiId) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("thalam").orderBy("thalam",true);;
            qb.where().le("thirumuraiId", thirumuraiId);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getThalamByCountry(String country) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("thalam").orderBy("thalam",true);
            qb.where().eq("country", country);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getPanbyThirumurai_Id(Integer thirumuraiId) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.distinct().selectColumns("pann").selectColumns("thirumuraiId").orderBy("title",false);
            qb.where().eq("thirumuraiId", thirumuraiId);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getAllFavouritesThirumurai() {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.where().eq("favorites", Boolean.TRUE);

            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getAll() {
        try {
            return thirumurai_songsDao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getSongsBySearchKeyByThalam(String searchKey) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.where().like("thalam", "%" + searchKey + "%");
            qb.distinct().selectColumns("thalam");
            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getSongsBySearchKeyByPann(String searchKey) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.where().like("pann", "%" + searchKey + "%");
            qb.distinct().selectColumns("thirumuraiId").selectColumns("pann");
            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getSongsBySearchKeyByCountry(String searchKey) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.where().like("country", "%" + searchKey + "%");
            qb.distinct().selectColumns("country");
            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getSongsBySearchKeyByAuthor(String searchKey) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.where().like("author", "%" + searchKey + "%");
            qb.distinct().selectColumns("author");
            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getSongsBySearchKeyByTitle(String searchKey) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.where().like("title", "%" + searchKey + "%");
            qb.distinct().selectColumns("title").orderBy("title",true);;
            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Thirumurai_songs> getSongsBySearchKeyBySongName(String searchKey) {
        try {
            QueryBuilder<Thirumurai_songs, String> qb = thirumurai_songsDao.queryBuilder();
            qb.where().like("song", "%" + searchKey + "%");
            qb.distinct().selectColumns("song").selectColumns("title").orderBy("title",true);;
            PreparedQuery<Thirumurai_songs> pq = qb.prepare();
            return thirumurai_songsDao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
