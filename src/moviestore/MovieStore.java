package moviestore;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the movie store operations including adding, removing, searching, and sorting movies.
 */
public class MovieStore {
    private List<Movie> movies;
    private static final String MANAGER_PASSWORD = "admin123";

    /**
     * Constructor for MovieStore. Initializes the movie list and loads movies from file.
     */
    public MovieStore() {
        movies = new ArrayList<>();
        loadMoviesFromFile("movies.txt");
    }

    /**
     * Loads movies from a text file.
     * @param filename The name of the file to load movies from.
     */
    private void loadMoviesFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    movies.add(new Movie(parts[0].trim(), parts[1].trim(), Integer.parseInt(parts[2].trim()), parts[3].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Adds a new movie to the store.
     * @param movie The movie to add.
     */
    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    /**
     * Removes a movie from the store by its title.
     * @param title The title of the movie to remove.
     * @return true if the movie was removed, false otherwise.
     */
    public boolean removeMovie(String title) {
        return movies.removeIf(movie -> movie.getTitle().equalsIgnoreCase(title));
    }

    /**
     * Displays all movies in the store.
     */
    public void displayAllMovies() {
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }

    /**
     * Displays a specific movie by its title.
     * @param title The title of the movie to display.
     */
    public void displayMovie(String title) {
        movies.stream()
                .filter(movie -> movie.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Movie not found.")
                );
    }

    /**
     * Sorts the movies based on the given criteria and algorithm.
     * @param sortBy The sorting criteria (title, actor, year, or genre).
     * @param algorithm The sorting algorithm to use.
     * @return The time taken to sort in milliseconds.
     */
    public long sortMovies(String sortBy, String algorithm) {
        Comparator<Movie> comparator = switch (sortBy.toLowerCase()) {
            case "title" -> Comparator.comparing(Movie::getTitle);
            case "actor" -> Comparator.comparing(Movie::getActor);
            case "year" -> Comparator.comparingInt(Movie::getYear);
            case "genre" -> Comparator.comparing(Movie::getGenre);
            default -> throw new IllegalArgumentException("Invalid sort criteria");
        };

        long startTime = System.currentTimeMillis();
        switch (algorithm.toLowerCase()) {
            case "bubblesort" -> bubbleSort(movies, comparator);
            case "selectionsort" -> selectionSort(movies, comparator);
            case "insertionsort" -> insertionSort(movies, comparator);
            case "mergesort" -> mergeSort(movies, comparator);
            default -> throw new IllegalArgumentException("Invalid sorting algorithm");
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * Implements the bubble sort algorithm.
     * @param list The list to sort.
     * @param comparator The comparator to use for sorting.
     */
    private void bubbleSort(List<Movie> list, Comparator<Movie> comparator) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    // Swap elements
                    Movie temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    /**
     * Implements the selection sort algorithm.
     * @param list The list to sort.
     * @param comparator The comparator to use for sorting.
     */
    private void selectionSort(List<Movie> list, Comparator<Movie> comparator) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(list.get(j), list.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            // Swap elements
            Movie temp = list.get(minIndex);
            list.set(minIndex, list.get(i));
            list.set(i, temp);
        }
    }

    /**
     * Implements the insertion sort algorithm.
     * @param list The list to sort.
     * @param comparator The comparator to use for sorting.
     */
    private void insertionSort(List<Movie> list, Comparator<Movie> comparator) {
        int n = list.size();
        for (int i = 1; i < n; ++i) {
            Movie key = list.get(i);
            int j = i - 1;

            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }
            list.set(j + 1, key);
        }
    }

    /**
     * Implements the merge sort algorithm.
     * @param list The list to sort.
     * @param comparator The comparator to use for sorting.
     */
    private void mergeSort(List<Movie> list, Comparator<Movie> comparator) {
        if (list.size() <= 1) {
            return;
        }
        int mid = list.size() / 2;
        List<Movie> left = new ArrayList<>(list.subList(0, mid));
        List<Movie> right = new ArrayList<>(list.subList(mid, list.size()));

        mergeSort(left, comparator);
        mergeSort(right, comparator);

        merge(list, left, right, comparator);
    }

    /**
     * Merges two sorted lists into one.
     * @param list The main list to merge into.
     * @param left The left sublist.
     * @param right The right sublist.
     * @param comparator The comparator to use for merging.
     */
    private void merge(List<Movie> list, List<Movie> left, List<Movie> right, Comparator<Movie> comparator) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) {
            list.set(k++, left.get(i++));
        }
        while (j < right.size()) {
            list.set(k++, right.get(j++));
        }
    }

    /**
     * Searches for movies using the specified algorithm.
     * @param query The search query.
     * @param searchBy The search criteria.
     * @param algorithm The search algorithm to use.
     * @return A Map containing the search results and the time taken in milliseconds.
     */
    public Map<String, Object> searchAlgorithm(String query, String searchBy, String algorithm) {
        long startTime = System.currentTimeMillis();
        List<Movie> results = switch (algorithm.toLowerCase()) {
            case "linear" -> linearSearch(query, searchBy);
            case "binary" -> binarySearch(query, searchBy);
            default -> throw new IllegalArgumentException("Invalid search algorithm");
        };
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("results", results);
        resultMap.put("timeTaken", timeTaken);
        return resultMap;
    }

    /**
     * Implements linear search algorithm.
     * @param query The search query.
     * @param searchBy The search criteria.
     * @return A list of movies matching the search criteria.
     */
    private List<Movie> linearSearch(String query, String searchBy) {
        return movies.stream()
                .filter(movie -> isMatch(movie, query, searchBy))
                .collect(Collectors.toList());
    }

    /**
     * Implements binary search algorithm.
     * @param query The search query.
     * @param searchBy The search criteria.
     * @return A list of movies matching the search criteria.
     */
    private List<Movie> binarySearch(String query, String searchBy) {
        // Sort the list first
        sortMovies(searchBy, "mergesort");

        List<Movie> result = new ArrayList<>();
        int left = 0;
        int right = movies.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Movie midMovie = movies.get(mid);
            int comparison;

            switch (searchBy.toLowerCase()) {
                case "title":
                    comparison = midMovie.getTitle().compareToIgnoreCase(query);
                    break;
                case "actor":
                    comparison = midMovie.getActor().compareToIgnoreCase(query);
                    break;
                case "year":
                    comparison = Integer.compare(midMovie.getYear(), Integer.parseInt(query));
                    break;
                case "genre":
                    comparison = midMovie.getGenre().compareToIgnoreCase(query);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid search criteria");
            }

            if (comparison == 0) {
                // Found a match, add it to the result and check adjacent elements
                addMatchingMovies(result, mid, query, searchBy);
                break;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    /**
     * Helper method to add all matching movies to the result list.
     * @param result The list to add matching movies to.
     * @param mid The index of the first match found.
     * @param query The search query.
     * @param searchBy The search criteria.
     */
    private void addMatchingMovies(List<Movie> result, int mid, String query, String searchBy) {
        // Check left
        int leftIndex = mid;
        while (leftIndex >= 0 && isMatch(movies.get(leftIndex), query, searchBy)) {
            result.add(movies.get(leftIndex));
            leftIndex--;
        }

        // Check right (excluding mid as it's already added)
        int rightIndex = mid + 1;
        while (rightIndex < movies.size() && isMatch(movies.get(rightIndex), query, searchBy)) {
            result.add(movies.get(rightIndex));
            rightIndex++;
        }
    }

    /**
     * Helper method to check if a movie matches the search criteria.
     * @param movie The movie to check.
     * @param query The search query.
     * @param searchBy The search criteria.
     * @return true if the movie matches, false otherwise.
     */
    private boolean isMatch(Movie movie, String query, String searchBy) {
        switch (searchBy.toLowerCase()) {
            case "title":
                return movie.getTitle().toLowerCase().contains(query.toLowerCase());
            case "actor":
                return movie.getActor().toLowerCase().contains(query.toLowerCase());
            case "year":
                return String.valueOf(movie.getYear()).equals(query);
            case "genre":
                return movie.getGenre().toLowerCase().contains(query.toLowerCase());
            default:
                return false;
        }
    }

    /**
     * Checks if the provided password is the manager password.
     * @param password The password to check.
     * @return true if the password is correct, false otherwise.
     */
    public static boolean isManagerPassword(String password) {
        return MANAGER_PASSWORD.equals(password);
    }

    /**
     * Writes the current list of movies to a file.
     * @param filename The name of the file to write to.
     * @throws IOException If an I/O error occurs.
     */
    public void writeMoviesToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Movie movie : movies) {
                writer.write(movie.toString());
                writer.newLine();
            }
        }
    }
}