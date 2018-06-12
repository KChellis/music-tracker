package models;

import java.util.Date;
import java.util.Objects;

public class Artist {
    private int id;
    private String name;
    private int yearStarted;

    public Artist(String name, int yearStarted) {
        this.name = name;
        this.yearStarted = yearStarted;
    }

    public String getName() {
        return name;
    }

    public int getYearStarted() {
        return yearStarted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id == artist.id &&
                yearStarted == artist.yearStarted &&
                Objects.equals(name, artist.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, yearStarted);
    }
}
