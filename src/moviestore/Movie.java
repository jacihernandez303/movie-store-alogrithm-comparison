package moviestore;

/**
 * Represents a movie with title, lead actor, release year, and genre.
 */
public class Movie {
    private String title;
    private String actor;
    private int year;
    private String genre;

    /**
     * Constructor for creating a new Movie object.
     * @param title The title of the movie.
     * @param actor The lead actor of the movie.
     * @param year The release year of the movie.
     * @param genre The genre of the movie.
     */
    public Movie(String title, String actor, int year, String genre) {
        this.title = title;
        this.actor = actor;
        this.year = year;
        this.genre = genre;
    }

    // Getters
    public String getTitle() { return title; }
    public String getActor() { return actor; }
    public int getYear() { return year; }
    public String getGenre() { return genre; }

    /**
     * Provides a string representation of the Movie object.
     * @return A string containing all the movie details.
     */
    @Override
    public String toString() {
        return String.format("Title: %s, Actor: %s, Year: %d, Genre: %s", title, actor, year, genre);
    }
}