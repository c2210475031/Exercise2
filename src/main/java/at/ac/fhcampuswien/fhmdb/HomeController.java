package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;
    public TextField releaseYearField;
    public JFXComboBox ratingComboBox;
    public JFXButton resetBtn;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    protected SortedState sortedState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();
        initializeLayout();
    }

    public void initializeState() {
        allMovies = MovieAPI.getAllMovies();
        observableMovies.clear();
        observableMovies.addAll(allMovies); // add all movies to the observable list
        sortedState = SortedState.NONE;
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell()); // apply custom cells to the listview

        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");

        ratingComboBox.setPromptText("Filter by Rating");
        String[] ratingText = new String[]{"1+","2+","3+","4+","5+","6+","7+","8+","9+"};
        ratingComboBox.getItems().add("No filter");
        ratingComboBox.getItems().addAll(ratingText);

        //https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        //ONLY NUMBERS CAN BE TYPED IN
        releaseYearField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                releaseYearField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    // sort movies based on sortedState
    // by default sorted state is NONE
    // afterwards it switches between ascending and descending
    public void sortMovies() {
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else if (sortedState == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    /**
     * When ButtonClicked API gets called with entered Filters
     * @param actionEvent
     */
    public void searchBtnClicked(ActionEvent actionEvent) {
        //Die Parameter werden hergerichtet zum überweisen
        String searchQuery = searchField.getText().trim().toLowerCase();
        String genre = null;
        if (genreComboBox.getSelectionModel().getSelectedIndex() > 0) {
            genre = genreComboBox.getSelectionModel().getSelectedItem().toString();
        }
        int releaseYear = releaseYearField.getText().isEmpty() ? 0 : Integer.parseInt(releaseYearField.getText());
        double ratingFrom = ratingComboBox.getSelectionModel().getSelectedIndex() > 0 ? ratingComboBox.getSelectionModel().getSelectedIndex() : 0;


        //System.out.println(searchQuery+genre+releaseYear+ratingFrom);

        //Gefilterte movieList wird von dem MovieAPI call erstellt (Anhand der Parameter)
        List<Movie> filteredMovies = MovieAPI.getFilteredMovies(searchQuery,genre,releaseYear,ratingFrom);
        //oberservable wird gecleared und mit der neuen Liste befuellt
        observableMovies.clear();
        observableMovies.addAll(filteredMovies);


        if(sortedState != SortedState.NONE) {
            sortMovies();
        }
    }
    public void resetBtnclick(ActionEvent actionEvent){
        searchField.setText("");
        genreComboBox.getSelectionModel().clearSelection();
        releaseYearField.setText("");
        ratingComboBox.getSelectionModel().clearSelection();
        observableMovies.clear();
        observableMovies.addAll(allMovies); // add all movies to the observable list
    }
    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }


    String getMostPopularActor(List<Movie> movies){

        //if there are no movies Odysseus is returned
        if (movies==null)return "Nobody";
        // actorStream is a stream made up of every actor
        Stream<String> actorStream = movies.stream()
                .flatMap(e -> e.getMainCast().stream());

        // Makes a map that combines every Name with how often it is included
        Map<String, Long> actorCounts = actorStream
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Find the actor with the highest count (only the first one found if there are multiple)
        Optional<Map.Entry<String, Long>> mostFrequentActor = actorCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        // Return the most frequent Actor or Odysseus
        return mostFrequentActor.map(Map.Entry::getKey).orElse("Nobody");
    }


    int getLongestMovieTitle(List<Movie> movies){
        if (movies==null)return -1;
        // AtomicInteger is because it
        AtomicInteger length= new AtomicInteger(0);

        // Check if every Titel is longer than the longest one found yet if so there is a new length
        movies.stream().forEach((e) -> {
            if(e.getTitle().length()> length.get()) length.set(e.getTitle().length());
        });
        return length.get();
    }

    long countMoviesFrom(List<Movie> movies, String director){
        AtomicLong count = new AtomicLong(0);

        if (movies==null)return -1;
        if (director==null)return -1;

        // go through each movie and if the director is in the movie increase the count
        movies.stream().forEach((e -> {
            if (e.directors.stream().anyMatch(director::equals)){
                count.set(count.get()+1);
            }
        }));

        return count.get();


    }

    List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear){
        if(movies==null)return null;
        if(startYear>endYear)return null;


        return movies.stream()
                //filter out everything that is too early
                .filter(e -> e.releaseYear >= startYear)
                //filter out everything that is too late
                .filter(e -> e.releaseYear <= endYear)
                //put the remaining movies in a list that we can return
                .collect(Collectors.toList());
    }
}