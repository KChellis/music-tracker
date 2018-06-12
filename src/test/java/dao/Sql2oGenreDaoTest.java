package dao;

import models.Artist;
import models.Genre;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;

public class Sql2oGenreDaoTest {
    private Connection conn;
    private Sql2oArtistDao artistDao;
    private  Sql2oGenreDao genreDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        artistDao = new Sql2oArtistDao(sql2o);
        genreDao = new Sql2oGenreDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingGenreSetsId() {
        Genre testGenre = setupGenre();
        assertEquals(1, testGenre.getId());
    }

    @Test
    public void getAll() {
        Genre testGenre = setupGenre();
        Genre testGenre2= setupAltGenre();
        assertEquals(2, genreDao.getAll().size());
    }

    @Test
    public void findById() {
        Genre testGenre = setupGenre();
        Genre testGenre2= setupAltGenre();
        assertEquals(testGenre.getName(), genreDao.findById(1).getName());
    }

    @Test
    public void deleteById() {
        Genre testGenre = setupGenre();
        Genre testGenre2= setupAltGenre();
        genreDao.deleteById(1);
        assertEquals(1, genreDao.getAll().size());
    }

    @Test
    public void deleteAll() {
        Genre testGenre = setupGenre();
        Genre testGenre2= setupAltGenre();
        genreDao.deleteAll();
        assertEquals(0, genreDao.getAll().size());
    }

    @Test
    public void getAllArtistsByGenreId() {
        Artist testArtist  = setupArtist();
        Artist otherArtist  = setupAltArtist();

        Genre testGenre  = setupGenre();

        artistDao.addGenreToArtist(testArtist, testGenre);
        artistDao.addGenreToArtist(otherArtist, testGenre);

        Artist[] artists = {testArtist, otherArtist};

        assertEquals(Arrays.asList(artists), genreDao.getAllArtistsByGenreId(testGenre.getId()));
    }

    @Test
    public void updateGenre_correctlyUpdatesGenre() {
        Genre testGenre = setupGenre();
        HashMap<String, Object> testMap = new HashMap<>();
        testMap.put("name", "rockabilly");
        genreDao.update(1, testMap);
        Genre updatedGenre = genreDao.findById(testGenre.getId());
        assertEquals("rockabilly", updatedGenre.getName());
    }

    public Genre setupGenre(){
        Genre genre = new Genre("punk rock");
        genreDao.add(genre);
        return genre;
    }

    public Genre setupAltGenre(){
        Genre genre = new Genre("rockabilly");
        genreDao.add(genre);
        return genre;
    }

    public Artist setupArtist(){
        Artist artist = new Artist("Blink-182", 1999);
        artistDao.add(artist);
        return artist;
    }

    public Artist setupAltArtist(){
        Artist altArtist = new Artist("Rolling Stones", 1962);
        artistDao.add(altArtist);
        return altArtist;
    }
}