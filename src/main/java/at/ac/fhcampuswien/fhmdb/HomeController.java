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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

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
        String searchQuery = searchField.getText().trim().toLowerCase();
        String genre = genreComboBox.getSelectionModel().getSelectedItem().toString();
        int releaseYear = Integer.parseInt(releaseYearField.getText());
        double ratingFrom = ratingComboBox.getSelectionModel().getSelectedIndex();

        //zum Sehen wie das aussieht
        //System.out.println(searchQuery+genre+releaseYear+ratingFrom);

        List<Movie> filteredMovies = MovieAPI.getFilteredMovies(searchQuery,genre,releaseYear,ratingFrom);
        observableMovies.clear();
        observableMovies.addAll(filteredMovies);


        //applyAllFilters(searchQuery, genre);

        if(sortedState != SortedState.NONE) {
            sortMovies();
        }
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }
}