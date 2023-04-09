package at.ac.fhcampuswien.fhmdb.models;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class MovieAPI {
    static OkHttpClient client = new OkHttpClient();

    static String baseUrl = "https://prog2.fh-campuswien.ac.at/movies";
    static String localBaseUrl ="http://localhost:8080/movies";


    /**
     * HTTP Get Request an die angegebene URL
     * @param url An welchen Server die Request gesendet werden soll
     * @return JSON String der response
     */
    public static String requestApi(String url){
        Request request = new Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent","http.agent")
                .build();

        Call call = client.newCall(request);
        Response response;
        try {
            response = call.execute();
            return response.body().string();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * JSON String wird zu einer Movie List geparsed
     * https://github.com/google/gson/blob/master/UserGuide.md#using-gson
     * @param json JSON String
     * @return Liste von Movies
     */
    public static List<Movie> jsonToMovies(String json){
        Gson gson = new Gson();
        TypeToken<List<Movie>> collectionType = new TypeToken<>() {};

        return gson.fromJson(json, collectionType.getType());
    }

    /**
     * Alle Available Movies der PROG API werden sich geholt
     * @return Liste der Movies die vorhanden waren
     */
    public static List<Movie> getAllMovies(){
        String json = requestApi(baseUrl);
        return jsonToMovies(json);
    }

    /**
     * Holt sich die gesuchten Movies von der API
     * @param query Text der vorhanden sein soll
     * @param genre Genre dass der gesuchte Movie haben soll
     * @param releaseYear der Movies die gesucht sind
     * @param ratingFrom ab diesem Rating wird gesucht
     * @return
     */
    public static List<Movie> getFilteredMovies(String query,String genre,int releaseYear,double ratingFrom){
        //HIER WIRD DANN DIE URL ZUSAMMENGEBAUT

        if ((query != null && !query.equals("")) || (genre != null && !genre.equals("")) || (releaseYear != 0) || (ratingFrom != 0)) {
            // build the URL with the search parameters that are present
            String url = baseUrl;
            if (query != null && !query.equals("")) {
                url += "?query=" + query;
            }
            if (genre != null && !genre.equals("")) {
                if (url.contains("?")) {
                    url += "&genre=" + genre;
                } else {
                    url += "?genre=" + genre;
                }
            }
            if (releaseYear != 0) {
                if (url.contains("?")) {
                    url += "&releaseYear=" + releaseYear;
                } else {
                    url += "?releaseYear=" + releaseYear;
                }
            }
            if (ratingFrom != 0) {
                if (url.contains("?")) {
                    url += "&ratingFrom=" + ratingFrom;
                } else {
                    url += "?ratingFrom=" + ratingFrom;
                }
            }

            String json = requestApi(url);
            return jsonToMovies(json);
        } else {
            // handle the case when no search parameters are present
            return getAllMovies();
        }
    }
}
