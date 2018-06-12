package dao;

import models.Artist;
import models.Genre;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oArtistDao implements ArtistDao {
    private final Sql2o sql2o;
    public Sql2oArtistDao(Sql2o sql2o) { this.sql2o = sql2o; }

    @Override
    public void add(Artist artist) {
        String sql = "INSERT INTO artists (name, yearStarted) VALUES (:name, :yearStarted)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .bind(artist)
                    .executeUpdate()
                    .getKey();
            artist.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void addGenreToArtist(Artist artist, Genre genre) {
        String sql = "INSERT INTO artists_genres (artistId, genreId) VALUES (:artistId, :genreId)";

        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("artistId", artist.getId())
                    .addParameter("genreId", genre.getId())
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Artist> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM artists")
                    .executeAndFetch(Artist.class);
        }

    }

    @Override
    public Artist findById(int id) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM artists WHERE id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Artist.class);
        }

    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from artists WHERE id=:id";
        String joinQuery = "DELETE from artists_genres WHERE artistId = :artistId";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
            con.createQuery(joinQuery)
                    .addParameter("artistId", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void deleteAll() {
        String sql = "DELETE from artists";
        String join = "DELETE from artist_genres";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).executeUpdate();
            con.createQuery(join).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }

    }

    @Override
    public List<Genre> getAllGenresByArtistId(int artistId) {
        ArrayList<Genre> genres = new ArrayList<>();

        String joinQuery = "SELECT genreId FROM artists_genres WHERE artistId = :artistId";

        try (Connection con = sql2o.open()) {
            List<Integer> allGenreIds = con.createQuery(joinQuery)
                    .addParameter("artistId", artistId)
                    .executeAndFetch(Integer.class);
            for (Integer genreId : allGenreIds){
                String genreQuery = "SELECT * FROM genres WHERE id = :genreId";
                genres.add(
                        con.createQuery(genreQuery)
                                .addParameter("genreId", genreId)
                                .executeAndFetchFirst(Genre.class));
            }
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
        return genres;
    }
}
