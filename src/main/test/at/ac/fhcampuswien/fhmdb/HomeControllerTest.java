package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private static HomeController homeController;
    private static List<Movie> movieList;
    @BeforeAll
    static void init() {
        homeController = new HomeController();
        movieList = MovieAPI.getAllMovies();
    }

    @Test
    void isMostPopularActorCorrect(){
        //should be leonardo di caprio or tom hanks
        String actor = homeController.getMostPopularActor(movieList);
        System.out.println(actor);
        assertEquals(  "Tom Hanks",actor);
    }

    @Test
    void isLongestMovieTitleCorrect(){

        int characterCountLongestMovie = homeController.getLongestMovieTitle(movieList);
        //46 wurde abgezaehlt => Star Wars Film
        assertEquals(46,characterCountLongestMovie);
    }

    @Test
    void isCountOfDirectorMoviesCorrect(){

        long count = homeController.countMoviesFrom(movieList,"Steven Spielberg");
        //Steven Spielberg hat 3 Filme
        assertEquals(3,count);

    }

    @Test
    void isMovielistBetweenTwoYearsCorrect(){
        List<Movie> filteredMovieList = homeController.getMoviesBetweenYears(movieList,2013,2019);
        //sollten 3 filme sein
        assertEquals(3,filteredMovieList.size());
    }





}