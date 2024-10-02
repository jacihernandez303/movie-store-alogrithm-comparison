package moviestore;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MovieStoreMain {
    private static final Scanner scanner = new Scanner(System.in);
    private static MovieStore store = new MovieStore();

    public static void main(String[] args) {
        boolean isManager = false;
        System.out.print("Enter password for manager mode (or press Enter for user mode): ");
        String password = scanner.nextLine();

        if (MovieStore.isManagerPassword(password)) {
            isManager = true;
            System.out.println("Manager mode activated.");
        } else {
            System.out.println("User mode activated.");
        }

        while (true) {
            displayMenu(isManager);
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> displayAllMovies();
                case 2 -> searchMovies();
                case 3 -> sortMovies();
                case 4 -> {
                    if (isManager) addMovie();
                    else System.out.println("Invalid option for user mode.");
                }
                case 5 -> {
                    if (isManager) removeMovie();
                    else System.out.println("Invalid option for user mode.");
                }
                case 0 -> {
                    System.out.println("Thank you for using the Movie Store Management System!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMenu(boolean isManager) {
        System.out.println("\n--- Movie Store Management System ---");
        System.out.println("1. Display all movies");
        System.out.println("2. Search movies");
        System.out.println("3. Sort movies");
        if (isManager) {
            System.out.println("4. Add a movie");
            System.out.println("5. Remove a movie");
        }
        System.out.println("0. Exit");
    }

    private static void displayAllMovies() {
        store.displayAllMovies();
    }

    private static void searchMovies() {
        System.out.print("Search by (title/actor/year/genre): ");
        String searchBy = scanner.nextLine();
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();
        String algorithm;
        do {
            System.out.print("Choose search algorithm (linear/binary): ");
            algorithm = scanner.nextLine().toLowerCase();
        } while (!algorithm.equals("linear") && !algorithm.equals("binary"));

        Map<String, Object> searchResult = store.searchAlgorithm(query, searchBy, algorithm);
        @SuppressWarnings("unchecked")
        List<Movie> results = (List<Movie>) searchResult.get("results");
        long timeTaken = (long) searchResult.get("timeTaken");

        if (results.isEmpty()) {
            System.out.println("No movies found.");
        } else {
            results.forEach(System.out::println);
        }
        System.out.println("Search completed in " + timeTaken + " milliseconds.");
    }

    private static void sortMovies() {
        System.out.print("Sort by (title/actor/year/genre): ");
        String sortBy = scanner.nextLine();
        System.out.print("Choose sorting algorithm (bubblesort/selectionsort/insertionsort/mergesort): ");
        String algorithm = scanner.nextLine();

        long timeTaken = store.sortMovies(sortBy, algorithm);
        System.out.println("Sorting completed in " + timeTaken + " milliseconds.");

        try {
            store.writeMoviesToFile("output.txt");
            System.out.println("Sorted movies have been written to output.txt");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        System.out.println("Sorted Movies:");
        store.displayAllMovies();
    }

    private static void addMovie() {
        System.out.print("Enter movie title: ");
        String title = scanner.nextLine();
        System.out.print("Enter lead actor/actress: ");
        String actor = scanner.nextLine();
        int year = getIntInput("Enter release year: ");
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        store.addMovie(new Movie(title, actor, year, genre));
        System.out.println("Movie added successfully.");
    }

    private static void removeMovie() {
        System.out.print("Enter the title of the movie to remove: ");
        String title = scanner.nextLine();
        if (store.removeMovie(title)) {
            System.out.println("Movie removed successfully.");
        } else {
            System.out.println("Movie not found.");
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}