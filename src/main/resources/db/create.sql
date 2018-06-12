SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS artists (
 id int PRIMARY KEY auto_increment,
 name VARCHAR,
 yearStarted int
);

CREATE TABLE IF NOT EXISTS genres (
 id int PRIMARY KEY auto_increment,
 name VARCHAR
);


CREATE TABLE IF NOT EXISTS artists_genres (
 id int PRIMARY KEY auto_increment,
 genreId INTEGER,
 artistId INTEGER
);
