package dao;

import models.Artist;
import models.Genre;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Sql2oArtistDaoTest {
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
    public void addingArtistSetsId() {
        Artist testArtist = setupArtist();
        assertEquals(1, testArtist.getId());
    }

    @Test
    public void addGenreToArtist() {
    }

    @Test
    public void getAll() {
        Artist testArtist1 = setupArtist();
        Artist testArtist2 = setupAltArtist();
        assertEquals(2, artistDao.getAll().size());
    }

    @Test
    public void findById() {
        Artist testArtist1 = setupArtist();
        Artist testArtist2 = setupAltArtist();
        assertEquals(testArtist1.getName(), artistDao.findById(1).getName());
    }

    @Test
    public void deleteById() {
        Artist testArtist1 = setupArtist();
        Artist testArtist2 = setupAltArtist();
        artistDao.deleteById(testArtist1.getId());
        assertEquals(1, artistDao.getAll().size());
    }

    @Test
    public void deleteAll() {
        Artist testArtist1 = setupArtist();
        Artist testArtist2 = setupAltArtist();
        artistDao.deleteAll();
        assertEquals(0, artistDao.getAll().size());
    }

    @Test
    public void getAllGenresByArtistId() {
        Genre testGenre  = new Genre("punk rock");
        genreDao.add(testGenre);

        Genre otherGenre  = new Genre("rockabilly");
        genreDao.add(otherGenre);

        Artist testArtist = setupArtist();
        artistDao.addGenreToArtist(testArtist, testGenre);
        artistDao.addGenreToArtist(testArtist, otherGenre);

        Genre[] genres = {testGenre, otherGenre};

        assertEquals(Arrays.asList(genres), artistDao.getAllGenresByArtistId(testArtist.getId()));
    }

    @Test
    public void deleteingArtistAlsoUpdatesJoinTable() throws Exception {
        Genre testGenre  = new Genre("Seafood");
        genreDao.add(testGenre);
        Artist testArtist = setupArtist();
        artistDao.add(testArtist);
        Artist altArtist = setupAltArtist();
        artistDao.add(altArtist);
        artistDao.addGenreToArtist(testArtist,testGenre);
        artistDao.addGenreToArtist(altArtist, testGenre);
        artistDao.deleteById(testArtist.getId());
        assertEquals(0, artistDao.getAllGenresByArtistId(testArtist.getId()).size());
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