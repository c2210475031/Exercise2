package at.ac.fhcampuswien.fhmdb.models;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;

import java.io.IOException;

public class MovieAPI {
    OkHttpClient client = new OkHttpClient();

    String url = "https://prog2.fh-campuswien.ac.at/movies";
    String genre = "ACTION";
    int releaseyear= 1933;
    double rating = 1;

    Request request = new Request.Builder()
            .url(url + "?genre=" + genre +"&releaseYear="+releaseyear+ "&ratingFrom=" + rating)
            .build();

        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        Movie movieResponse = new Gson().fromJson(jsonData, Movie.class);




    public MovieAPI() throws IOException {
    }
}
