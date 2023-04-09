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
    @BeforeAll
    static void init() {
        homeController = new HomeController();
    }

    @Test
    void isMostPopularActorCorrect(){

    }

    @Test
    void isLongestMovieTitleCorrect(){
        List<Movie> movieList = MovieAPI.getAllMovies();
        int characterCountLongestMovie = homeController.getLongestMovieTitle(movieList);
        assertEquals(46,characterCountLongestMovie);
    }

    @Test
    void isCountOfDirectorMoviesCorrect(){

    }

    @Test
    void isMovielistBetweenTwoYearsCorrect(){

    }





}