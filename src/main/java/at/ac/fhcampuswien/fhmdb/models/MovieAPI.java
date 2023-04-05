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

    String baseUrl = "https://prog2.fh-campuswien.ac.at/movies";
    static String localBaseUrl ="http://localhost:8080/movies";



    public static String requestApi(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        Response response;
        try {
            response = call.execute();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return response.body().toString();
    }

    /**
     * https://github.com/google/gson/blob/master/UserGuide.md#using-gson
     * @param json JSON String
     * @return Liste von Movies
     */
    public static List<Movie> jsonToMovies(String json){
        Gson gson = new Gson();
        TypeToken<List<Movie>> collectionType = new TypeToken<>() {};
        return gson.fromJson(json, collectionType);
    }

    public static List<Movie> getAllMovies(){
        String json = requestApi(localBaseUrl);
        return jsonToMovies(json);
    }

}
