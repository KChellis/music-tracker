package models;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArtistTest {

    @Test
    public void newArtist_instantiatesCorrectly() {
        Artist testArtist = setupArtist();
        assertTrue(testArtist instanceof Artist);
    }

    @Test
    public void getName_instantiatesWithName() {
        Artist testArtist = setupArtist();
        assertEquals("Blink-182", testArtist.getName());
    }

    @Test
    public void getYearStarted() {
        Artist testArtist = setupArtist();
        assertEquals(1999, testArtist.getYearStarted());
    }

    @Test
    public void setId() {
        Artist testArtist = setupArtist();
        testArtist.setId(1);
        assertEquals(1, testArtist.getId());
    }

    public Artist setupArtist(){
        return new Artist("Blink-182", 1999);
    }
}