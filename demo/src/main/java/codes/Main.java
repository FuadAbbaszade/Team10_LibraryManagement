package codes;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ReadBook read = new ReadBook();
        String filepath = "C:\\Users\\fuada\\Desktop\\Library\\app\\src\\main\\java\\codes\\books.csv";
        Library library = new Library();

        read.loadBooks(filepath, library);
        library.listBooks();

        Scanner sc = new Scanner(System.in);
        Sort sorter = new Sort();
        boolean running = true;

        while (running) {
            System.out.println("\n--- Library Menu ---");
            System.out.println("1. Add Book");
            System.out.println("2. List Books");
            System.out.println("3. Search Books");
            System.out.println("4. Sort Books");
            System.out.println("5. Filter Books");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    // add a new book
                    System.out.print("Enter title: ");
                    String title = sc.nextLine();

                    System.out.print("Enter genre: ");
                    String genre = sc.nextLine();

                    System.out.print("Enter publisher: ");
                    String publisher = sc.nextLine();

                    System.out.print("Enter publication date: ");
                    String date = sc.nextLine();

                    System.out.print("Enter page count: ");
                    int pageCount = 0;
                    try {
                        pageCount = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number. Setting page count to 0.");
                    }

                    System.out.print("Enter author name: ");
                    String authorName = sc.nextLine();

                    System.out.print("Enter author nationality: ");
                    String nationality = sc.nextLine();

                    Author author = new Author(authorName, nationality);
                    Book book = new Book(title, author, publisher, date, pageCount, genre);

                    library.addBook(book);
                    read.saveBook(library.getBooks(), filepath);
                    System.out.println("Book added successfully.");
                    break;

                case "2":
                    // List all books
                    library.listBooks();
                    break;

                case "3":
                    // Search books
                    System.out.println("Search by which field?");
                    System.out.println("Options: title, author, publisher, date, pagecount, genre");
                    System.out.print("Enter search field: ");
                    String searchField = sc.nextLine();

                    System.out.print("Enter search value: ");
                    String searchValue = sc.nextLine();

                    List<Book> results = library.searchBooks(searchField, searchValue);

                    if (results.isEmpty()) {
                        System.out.println("No matching books found.");
                    } else {
                        System.out.println("Found " + results.size() + " book(s):");
                        int idx = 0;
                        for (Book b : results) {
                            idx++;
                            System.out.println(idx + ". " + b);
                        }

                        // Optionally delete
                        System.out.print("Delete a book? (y/n): ");
                        String del = sc.nextLine();

                        if (del.equalsIgnoreCase("y") && results.size() == 1) {
                            library.deleteBook(results.get(0));
                            ReadBook.saveBook(library.getBooks(), filepath);
                            System.out.println("Deleted first matching book.");
                        } else if (del.equalsIgnoreCase("y") && results.size() > 1) {
                            boolean validInput = false;
                            while (!validInput) {
                                System.out.print("Enter the number of the book to delete (or 0 to cancel): ");
                                String input = sc.nextLine();
                                try {
                                    int delindex = Integer.parseInt(input);
                                    if (delindex == 0) {
                                        System.out.println("Deletion cancelled.");
                                        validInput = true;
                                    } else if (delindex >= 1 && delindex <= results.size()) {
                                        library.deleteBook(results.get(delindex - 1));
                                        read.saveBook(library.getBooks(), filepath);
                                        System.out.println("Deleted book number " + delindex + ".");
                                        validInput = true;
                                    } else {
                                        System.out.println("Invalid number. Please try again.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a number.");
                                }
                            }
                        }

                        // Optionally update
                        System.out.print("Update a book? (y/n): ");
                        String upd = sc.nextLine();

                        if (upd.equalsIgnoreCase("y")) {
                            if (results.size() == 1) {
                                library.updateBook(results.get(0), sc);
                                read.saveBook(library.getBooks(), filepath);
                                System.out.println("Updated book.");
                            } else {
                                boolean validInput = false;
                                while (!validInput) {
                                    System.out.print("Enter the number of the book to update (or 0 to cancel): ");
                                    String input = sc.nextLine();
                                    try {
                                        int updIndex = Integer.parseInt(input);
                                        if (updIndex == 0) {
                                            System.out.println("Update cancelled.");
                                            validInput = true;
                                        } else if (updIndex >= 1 && updIndex <= results.size()) {
                                            library.updateBook(results.get(updIndex - 1), sc);
                                            read.saveBook(library.getBooks(), filepath);
                                            System.out.println("Updated book number " + updIndex + ".");
                                            validInput = true;
                                        } else {
                                            System.out.println("Invalid number. Please try again.");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid input. Please enter a number.");
                                    }
                                }
                            }
                        }
                    }
                    break;

                case "4":
                    // Sort books
                    System.out.println("Sort by which field?");
                    System.out.println("Options: title, author, publisher, date, pages, genre");
                    System.out.print("Enter sort field: ");
                    String sortKey = sc.nextLine();

                    sorter.sortBooks(library.getBooks(), sortKey);
                    System.out.println("Books sorted by " + sortKey + ".");
                    library.listBooks();
                    break;


                case "5":

                    // Filter books
                    Filter filter = new Filter();
                    System.out.print("Enter genre to filter (or leave blank): ");
                    String fGenre = sc.nextLine();
                    System.out.print("Enter author name to filter (or leave blank): ");
                    String fAuthor = sc.nextLine();
                    System.out.print("Enter minimum pages to filter (or 0 to ignore): ");
                    int fPages = 0;
                    try {
                        fPages = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number. Using 0.");
                    }

                    List<Book> filtered = filter.filterBooks(library.getBooks(), fGenre, fAuthor, fPages);

                    if (filtered.isEmpty()) {
                        System.out.println("No books match the filter.");
                    } else {
                        System.out.println("Filtered books:");
                        for (Book b : filtered) {
                            System.out.println(b);
                        }
                    }
                    break;


                 case "6":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                
                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }
        sc.close();
    }
}
