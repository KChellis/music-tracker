package dao;

import models.Artist;
import models.Genre;

import java.util.List;

public interface GenreDao {
    //CREATE
    void add(Genre genre);

    //READ
    List<Genre> getAll();
    Genre findById(int id);
    List<Artist> getAllArtistsByGenreId(int genreId);

    //UPDATE

    //DELETE
    void deleteById(int id);
    void deleteAll();
}
