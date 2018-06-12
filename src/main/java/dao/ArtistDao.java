package dao;

import models.Artist;
import models.Genre;

import java.util.List;

public interface ArtistDao {

    //CREATE
    void add(Artist artist);
    void addGenreToArtist(Artist artist, Genre genre);

    //READ
    List<Artist> getAll();
    Artist findById(int id);
    List<Genre> getAllGenresByArtistId(int artistId);

    //UPDATE

    //DELETE
    void deleteById(int id);
    void deleteAll();
}
