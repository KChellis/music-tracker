package models;

import org.junit.Test;

import static org.junit.Assert.*;

public class GenreTest {

    @Test
    public void newGenre_genreInstantiatesWithName() {
        Genre testGenre = setupGenre();
        assertTrue(testGenre instanceof Genre);
    }

    @Test
    public void getName_genreInstantiatesWithName() {
        Genre testGenre = setupGenre();
        assertEquals("punk rock", testGenre.getName());
    }

    @Test
    public void setId_setsId() {
        Genre testGenre = setupGenre();
        testGenre.setId(1);
        assertEquals(1, testGenre.getId());
    }

    public Genre setupGenre(){
        return new Genre("punk rock");
    }
}