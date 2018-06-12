package dao;

import models.Artist;
import models.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ArtistDao {

    //CREATE
    void add(Artist artist);
    void addGenreToArtist(Artist artist, Genre genre);

    //READ
    List<Artist> getAll();
    Artist findById(int id);
    List<Genre> getAllGenresByArtistId(int artistId);

    //UPDATE
    void update(int id, HashMap<String, Object> updatedContent);

    //DELETE
    void deleteById(int id);
    void deleteAll();
}
