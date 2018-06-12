package dao;

import models.Artist;
import models.Genre;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oGenreDao implements GenreDao{
    private final Sql2o sql2o;
    public Sql2oGenreDao(Sql2o sql2o) { this.sql2o = sql2o; }

    @Override
    public void add(Genre genre) {
        String sql = "INSERT INTO genres (name) VALUES (:name)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .bind(genre)
                    .executeUpdate()
                    .getKey();
            genre.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Genre> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM genres")
                    .executeAndFetch(Genre.class);
        }
    }

    @Override
    public Genre findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM genres WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Genre.class);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from genres WHERE id=:id";
        String joinQuery = "DELETE from artists_genres WHERE genreId = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
            con.createQuery(joinQuery)
                    .addParameter("genreId", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE from genres";
        String join = "DELETE from artist_genres";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).executeUpdate();
            con.createQuery(join).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public List<Artist> getAllArtistsByGenreId(int genreId) {
        ArrayList<Artist> artists = new ArrayList<>();

        String joinQuery = "SELECT artistId FROM artists_genres WHERE genreId = :genreId";

        try (Connection con = sql2o.open()) {
            List<Integer> allArtistIds = con.createQuery(joinQuery)
                    .addParameter("genreId", genreId)
                    .executeAndFetch(Integer.class);
            for (Integer artistId : allArtistIds){
                String artistQuery = "SELECT * FROM artists WHERE id = :artistId";
                artists.add(
                        con.createQuery(artistQuery)
                                .addParameter("artistId", artistId)
                                .executeAndFetchFirst(Artist.class));
            }
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
        return artists;
    }
}
