import com.google.gson.Gson;
import dao.Sql2oArtistDao;
import dao.Sql2oGenreDao;
import exceptions.ApiException;
import models.Artist;
import models.Genre;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        Sql2oArtistDao artistDao;
        Sql2oGenreDao genreDao;
        Connection conn;
        Gson gson = new Gson();

        String connectionString = "jdbc:h2:~/music-tracker.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");

        artistDao = new Sql2oArtistDao(sql2o);
        genreDao = new Sql2oGenreDao(sql2o);
        conn = sql2o.open();

        post("/artists/new", "application/json", (req, res) -> {
            Artist artist = gson.fromJson(req.body(), Artist.class);
            artistDao.add(artist);
            res.status(201);
            return gson.toJson(artist);
        });

        post("/genres/new", "application/json", (req, res) -> {
            Genre genre = gson.fromJson(req.body(), Genre.class);
            genreDao.add(genre);
            res.status(201);
            return gson.toJson(genre);
        });

        get("/artists", "application/json", (req, res) -> {
            return gson.toJson(artistDao.getAll());
        });

        get("/genres", "application/json", (req, res) -> {
            return gson.toJson(genreDao.getAll());
        });

        get("/artists/delete", "application/json", (req, res) -> {
            artistDao.deleteAll();
            return "{\"message\":\"All artists deleted.\"}";
        });

        get("/genres/delete", "application/json", (req, res) -> {
            genreDao.deleteAll();
            return "{\"message\":\"All genres deleted.\"}";
        });

        get("/artists/:artistId", "application/json", (req, res) -> {
            int artistId = Integer.parseInt(req.params("artistId"));
            return gson.toJson(artistDao.findById(artistId));
        });

        get("/artists/:artistId/delete", "application/json", (req, res) -> {
            int artistId = Integer.parseInt(req.params("artistId"));
            artistDao.deleteById(artistId);
            return "{\"message\":\"Artist deleted.\"}";
        });

        get("/genre/:genreId/delete", "application/json", (req, res) -> {
            int genreId = Integer.parseInt(req.params("genreId"));
            genreDao.deleteById(genreId);
            return "{\"message\":\"Genre deleted.\"}";
        });

        get("/artist/:artistId/genres", "application/json", (req, res) -> {
           int artistId = Integer.parseInt(req.params("artistId"));
           Artist artistToFind = artistDao.findById(artistId);
           if(artistToFind == null){
               throw new ApiException(404, String.format("No artist with id: \"%d\" exists", artistId));
           }else if (artistDao.getAllGenresByArtistId(artistId).size() ==0){
               return "{\"message\":\"There are no genres listed for this artist.\"}";
           }else {
               return gson.toJson(artistDao.getAllGenresByArtistId(artistId));
           }
        });

        get("/genres/:genreId/artists", "application/json", (req, res) -> {
            int genreId = Integer.parseInt(req.params("genreId"));
            Genre genreToFind = genreDao.findById(genreId);
            if(genreToFind == null){
                throw new ApiException(404, String.format("No genre with id: \"%d\" exists", genreId));
            }else if (genreDao.getAllArtistsByGenreId(genreId).size() ==0){
                return "{\"message\":\"There are no artists listed for this genre.\"}";
            }else {
                return gson.toJson(genreDao.getAllArtistsByGenreId(genreId));
            }
        });

        post("/artists/artistId/genres/genreId", "application/json", (req, res) -> {
            int artistId = Integer.parseInt(req.params("artistId"));
            int genreId = Integer.parseInt(req.params("genreId"));
            Artist artist = artistDao.findById(artistId);
            Genre genre = genreDao.findById(genreId);

            if(artist != null && genre != null){
                artistDao.addGenreToArtist(artist, genre);
                res.status(201);
                return gson.toJson(String.format("%s and %s have been associated", artist.getName(), genre.getName()));
            } else {
                throw new ApiException(404, String.format("Artist or genre does not exist"));
            }
        });

        post("/artists/:artistId/update", "application/json", (req, res) -> {
            System.out.println("I'm here");
            HashMap<String, Object> updatedContent = gson.fromJson(req.body(), HashMap.class);
            System.out.println("I'm there");
            int artistId = Integer.parseInt(req.params("artistId"));
            artistDao.update(artistId, updatedContent);
            res.status(201);
            return gson.toJson(artistDao.findById(artistId));
        });

        exception(ApiException.class, (exception, req, res) -> {
            ApiException err = (ApiException) exception;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatusCode());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatusCode());
            res.body(gson.toJson(jsonMap));
        });

        after((req, res) ->{
            res.type("application/json");
        });



    }
}
