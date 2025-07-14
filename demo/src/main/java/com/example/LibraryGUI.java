package com.example;

import java.io.File;
import java.util.List;
import java.util.Optional;

import codes.Author;
import codes.Book;
import codes.Filter;
import codes.Library;
import codes.ReadBook;
import codes.Sort;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LibraryGUI extends Application {
    private Library library;
    private ReadBook readBook;
    private TableView<Book> bookTable;
    private ObservableList<Book> bookData;
    private String filepath = "C:\\Users\\fuada\\Downloads\\project\\project\\demo\\src\\main\\java\\codes\\books.csv";

    private Label totalBooksLabel; // Added reference for dynamic updates
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize library components
        library = new Library();
        readBook = new ReadBook();
        
        // Load books from CSV with error handling
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                showAlert("File Not Found", "CSV file not found at: " + filepath + "\nStarting with empty library.");
            } else {
                readBook.loadBooks(filepath, library);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load books: " + e.getMessage() + "\nStarting with empty library.");
        }
        
        // Create the main layout
        BorderPane root = new BorderPane();
        
        // Create and setup the book table
        setupBookTable();
        
        // Create control panels
        VBox leftPanel = createLeftPanel();
        VBox rightPanel = createRightPanel();
        
        // Setup the layout
        root.setCenter(bookTable);
        root.setLeft(leftPanel);
        root.setRight(rightPanel);
        root.setTop(createTopPanel());
        
        // Create scene and show
        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Refresh table data
        refreshTable();
    }
    
    private Label createTopPanel() {
        Label title = new Label("📚 Library Management System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setPadding(new Insets(10));
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
        return title;
    }
    
    private void setupBookTable() {
    bookTable = new TableView<>();
    bookData = FXCollections.observableArrayList();
    
    // Title column
    TableColumn<Book, String> titleCol = new TableColumn<>("Title");
    titleCol.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().getTitle()));
    titleCol.setPrefWidth(200);
    
    // Author column
    TableColumn<Book, String> authorCol = new TableColumn<>("Author");
    authorCol.setCellValueFactory(cellData -> {
        Author author = cellData.getValue().getAuthor();
        return new SimpleStringProperty(author != null ? author.getFullName() : "Unknown");
    });
    authorCol.setPrefWidth(150);
    
    // Nationality column
    TableColumn<Book, String> nationalityCol = new TableColumn<>("Nationality");
    nationalityCol.setCellValueFactory(cellData -> {
        Author author = cellData.getValue().getAuthor();
        return new SimpleStringProperty(author != null ? author.getNationality() : "Unknown");
    });
    nationalityCol.setPrefWidth(100);
    
    // Publisher column (lambda)
    TableColumn<Book, String> publisherCol = new TableColumn<>("Publisher");
    publisherCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(
            cellData.getValue().getPublisher() != null ? cellData.getValue().getPublisher() : "Unknown"
        ));
    publisherCol.setPrefWidth(120);
    
    // Date column (lambda)
    TableColumn<Book, String> dateCol = new TableColumn<>("Date");
    dateCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(
            cellData.getValue().getPublicationDate() != null ? cellData.getValue().getPublicationDate() : "Unknown"
        ));
    dateCol.setPrefWidth(80);
    
    // Pages column (lambda with formatted string)
    TableColumn<Book, String> pagesCol = new TableColumn<>("Pages");
    pagesCol.setCellValueFactory(cellData -> 
        new SimpleStringProperty(
            String.valueOf(cellData.getValue().getPageCount())
        ));
    pagesCol.setPrefWidth(80);
    
    // Genre column (lambda)
    TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
    genreCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(
            cellData.getValue().getGenre() != null ? cellData.getValue().getGenre() : "Unknown"
        ));
    genreCol.setPrefWidth(120);
    
    bookTable.getColumns().addAll(titleCol, authorCol, nationalityCol, publisherCol, dateCol, pagesCol, genreCol);
    bookTable.setItems(bookData);
}
    
    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setPrefWidth(250);
        leftPanel.setStyle("-fx-background-color: #ecf0f1;");
        
        // Add Book Section
        Label addLabel = new Label("📖 Add New Book");
        addLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Button addButton = new Button("Add Book");
        addButton.setPrefWidth(200);
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        addButton.setOnAction(e -> showAddBookDialog());
        
        // File operations
        Button loadButton = new Button("Load CSV File");
        loadButton.setPrefWidth(200);
        loadButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;");
        loadButton.setOnAction(e -> loadCSVFile());
        
        // Search Section
        Label searchLabel = new Label("🔍 Search Books");
        searchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        ComboBox<String> searchField = new ComboBox<>();
        searchField.getItems().addAll("title", "author", "publisher", "date", "pagecount", "genre");
        searchField.setValue("title");
        searchField.setPrefWidth(200);
        
        TextField searchValue = new TextField();
        searchValue.setPromptText("Search value...");
        searchValue.setPrefWidth(200);
        
        Button searchButton = new Button("Search");
        searchButton.setPrefWidth(200);
        searchButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        searchButton.setOnAction(e -> performSearch(searchField.getValue(), searchValue.getText()));
        
        Button clearButton = new Button("Show All");
        clearButton.setPrefWidth(200);
        clearButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
        clearButton.setOnAction(e -> refreshTable());
        
        leftPanel.getChildren().addAll(
            addLabel, addButton, loadButton,
            new Separator(),
            searchLabel, searchField, searchValue, searchButton, clearButton
        );
        
        return leftPanel;
    }
    
    private VBox createRightPanel() {
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setPrefWidth(250);
        rightPanel.setStyle("-fx-background-color: #ecf0f1;");
        
        // Book Actions Section
        Label actionsLabel = new Label("⚙️ Book Actions");
        actionsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Button updateButton = new Button("Update Selected");
        updateButton.setPrefWidth(200);
        updateButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        updateButton.setOnAction(e -> updateSelectedBook());
        
        Button deleteButton = new Button("Delete Selected");
        deleteButton.setPrefWidth(200);
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> deleteSelectedBook());
        
        // Sort Section
        Label sortLabel = new Label("📊 Sort Books");
        sortLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        ComboBox<String> sortField = new ComboBox<>();
        sortField.getItems().addAll("title", "author", "publisher", "date", "pages", "genre");
        sortField.setValue("title");
        sortField.setPrefWidth(200);
        
        Button sortButton = new Button("Sort");
        sortButton.setPrefWidth(200);
        sortButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");
        sortButton.setOnAction(e -> sortBooks(sortField.getValue()));
        
        // Filter Section
        Label filterLabel = new Label("🔽 Filter Books");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Button filterButton = new Button("Advanced Filter");
        filterButton.setPrefWidth(200);
        filterButton.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white;");
        filterButton.setOnAction(e -> showFilterDialog());
        
        // Stats
        Label statsLabel = new Label("📈 Library Stats");
        statsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        totalBooksLabel = new Label("Total Books: " + (library != null ? library.getBooks().size() : 0));
        
        rightPanel.getChildren().addAll(
            actionsLabel, updateButton, deleteButton,
            new Separator(),
            sortLabel, sortField, sortButton,
            new Separator(),
            filterLabel, filterButton,
            new Separator(),
            statsLabel, totalBooksLabel
        );
        
        return rightPanel;
    }
    
    private void loadCSVFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                filepath = selectedFile.getAbsolutePath();
                library = new Library(); // Reset library
                readBook.loadBooks(filepath, library);
                refreshTable();
                showAlert("Success", "CSV file loaded successfully!");
            } catch (Exception e) {
                showAlert("Error", "Failed to load CSV file: " + e.getMessage());
            }
        }
    }
    
    private void showAddBookDialog() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add New Book");
        dialog.setHeaderText("Enter book details:");
        
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField nationalityField = new TextField();
        TextField publisherField = new TextField();
        TextField dateField = new TextField();
        TextField pagesField = new TextField();
        TextField genreField = new TextField();
        
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Nationality:"), 0, 2);
        grid.add(nationalityField, 1, 2);
        grid.add(new Label("Publisher:"), 0, 3);
        grid.add(publisherField, 1, 3);
        grid.add(new Label("Date:"), 0, 4);
        grid.add(dateField, 1, 4);
        grid.add(new Label("Pages:"), 0, 5);
        grid.add(pagesField, 1, 5);
        grid.add(new Label("Genre:"), 0, 6);
        grid.add(genreField, 1, 6);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String title = titleField.getText().trim();
                    String authorName = authorField.getText().trim();
                    String nationality = nationalityField.getText().trim();
                    String publisher = publisherField.getText().trim();
                    String date = dateField.getText().trim();
                    String pagesText = pagesField.getText().trim();
                    String genre = genreField.getText().trim();
                    
                    // Validation
                    if (title.isEmpty() || authorName.isEmpty()) {
                        showAlert("Error", "Title and Author are required fields.");
                        return null;
                    }
                    
                    if (pagesText.isEmpty()) {
                        showAlert("Error", "Pages field is required.");
                        return null;
                    }
                    
                    int pages = Integer.parseInt(pagesText);
                    if (pages <= 0) {
                        showAlert("Error", "Pages must be a positive number.");
                        return null;
                    }
                    
                    // Set default values for empty fields
                    if (nationality.isEmpty()) nationality = "Unknown";
                    if (publisher.isEmpty()) publisher = "Unknown";
                    if (date.isEmpty()) date = "Unknown";
                    if (genre.isEmpty()) genre = "Unknown";
                    
                    Author author = new Author(authorName, nationality);
                    return new Book(title, author, publisher, date, pages, genre);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid number for pages.");
                    return null;
                }
            }
            return null;
        });
        
        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            library.addBook(book);
            try {
                readBook.saveBook(library.getBooks(), filepath);
                refreshTable();
                showAlert("Success", "Book added successfully!");
            } catch (Exception e) {
                showAlert("Error", "Book added but failed to save to file: " + e.getMessage());
                refreshTable(); // Still refresh to show the added book
            }
        });
    }
    
    private void performSearch(String field, String value) {
        if (value.trim().isEmpty()) {
            refreshTable();
            return;
        }
        
        try {
            List<Book> results = library.searchBooks(field, value);
            bookData.clear();
            bookData.addAll(results);
            
            if (results.isEmpty()) {
                showAlert("Search Results", "No books found matching your search.");
            }
        } catch (Exception e) {
            showAlert("Error", "Search failed: " + e.getMessage());
            refreshTable();
        }
    }
    
    private void sortBooks(String sortKey) {
        try {
            Sort sorter = new Sort();
            sorter.sortBooks(library.getBooks(), sortKey);
            refreshTable();
        } catch (Exception e) {
            showAlert("Error", "Sort failed: " + e.getMessage());
        }
    }
    
    private void showFilterDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Filter Books");
        dialog.setHeaderText("Set filter criteria:");
        
        ButtonType filterButtonType = new ButtonType("Apply Filter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(filterButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField genreField = new TextField();
        genreField.setPromptText("Enter genre (or leave blank)");
        TextField authorField = new TextField();
        authorField.setPromptText("Enter author name (or leave blank)");
        TextField pagesField = new TextField();
        pagesField.setPromptText("Enter minimum pages (or leave blank)");
        
        grid.add(new Label("Genre:"), 0, 0);
        grid.add(genreField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Min Pages:"), 0, 2);
        grid.add(pagesField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == filterButtonType) {
                String genre = genreField.getText().trim();
                String author = authorField.getText().trim();
                int minPages = 0;
                
                try {
                    if (!pagesField.getText().trim().isEmpty()) {
                        minPages = Integer.parseInt(pagesField.getText().trim());
                        if (minPages < 0) {
                            showAlert("Error", "Minimum pages cannot be negative.");
                            return null;
                        }
                    }
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid number for minimum pages.");
                    return null;
                }
                
                try {
                    Filter filter = new Filter();
                    List<Book> filtered = filter.filterBooks(library.getBooks(), genre, author, minPages);
                    
                    bookData.clear();
                    bookData.addAll(filtered);
                    
                    if (filtered.isEmpty()) {
                        showAlert("Filter Results", "No books match the filter criteria.");
                    }
                } catch (Exception e) {
                    showAlert("Error", "Filter failed: " + e.getMessage());
                }
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private void updateSelectedBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("No Selection", "Please select a book to update.");
            return;
        }
        
        // Create update dialog (similar to add dialog but pre-filled)
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Update Book");
        dialog.setHeaderText("Update book details:");
        
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField titleField = new TextField(selectedBook.getTitle());
        TextField authorField = new TextField(selectedBook.getAuthor() != null ? selectedBook.getAuthor().getFullName() : "");
        TextField nationalityField = new TextField(selectedBook.getAuthor() != null ? selectedBook.getAuthor().getNationality() : "");
        TextField publisherField = new TextField(selectedBook.getPublisher());
        TextField dateField = new TextField(selectedBook.getPublicationDate());
        TextField pagesField = new TextField(String.valueOf(selectedBook.getPageCount()));
        TextField genreField = new TextField(selectedBook.getGenre());
        
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Nationality:"), 0, 2);
        grid.add(nationalityField, 1, 2);
        grid.add(new Label("Publisher:"), 0, 3);
        grid.add(publisherField, 1, 3);
        grid.add(new Label("Date:"), 0, 4);
        grid.add(dateField, 1, 4);
        grid.add(new Label("Pages:"), 0, 5);
        grid.add(pagesField, 1, 5);
        grid.add(new Label("Genre:"), 0, 6);
        grid.add(genreField, 1, 6);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    String title = titleField.getText().trim();
                    String authorName = authorField.getText().trim();
                    String nationality = nationalityField.getText().trim();
                    String publisher = publisherField.getText().trim();
                    String date = dateField.getText().trim();
                    String pagesText = pagesField.getText().trim();
                    String genre = genreField.getText().trim();
                    
                    // Validation
                    if (title.isEmpty() || authorName.isEmpty()) {
                        showAlert("Error", "Title and Author are required fields.");
                        return null;
                    }
                    
                    if (pagesText.isEmpty()) {
                        showAlert("Error", "Pages field is required.");
                        return null;
                    }
                    
                    int pages = Integer.parseInt(pagesText);
                    if (pages <= 0) {
                        showAlert("Error", "Pages must be a positive number.");
                        return null;
                    }
                    
                    // Set default values for empty fields
                    if (nationality.isEmpty()) nationality = "Unknown";
                    if (publisher.isEmpty()) publisher = "Unknown";
                    if (date.isEmpty()) date = "Unknown";
                    if (genre.isEmpty()) genre = "Unknown";
                    
                    selectedBook.setTitle(title);
                    if (selectedBook.getAuthor() != null) {
                        selectedBook.getAuthor().setFullname(authorName); // Fixed method name
                        selectedBook.getAuthor().setNationality(nationality);
                    } else {
                        selectedBook.setAuthor(new Author(authorName, nationality));
                    }
                    selectedBook.setPublisher(publisher);
                    selectedBook.setPublicationDate(date);
                    selectedBook.setPageCount(pages);
                    selectedBook.setGenre(genre);
                    
                    return selectedBook;
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid number for pages.");
                    return null;
                }
            }
            return null;
        });
        
        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            try {
                readBook.saveBook(library.getBooks(), filepath);
                refreshTable();
                showAlert("Success", "Book updated successfully!");
            } catch (Exception e) {
                showAlert("Error", "Book updated but failed to save to file: " + e.getMessage());
                refreshTable(); // Still refresh to show the updated book
            }
        });
    }
    
    private void deleteSelectedBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("No Selection", "Please select a book to delete.");
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Book");
        confirmAlert.setContentText("Are you sure you want to delete: " + selectedBook.getTitle() + "?");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            library.deleteBook(selectedBook);
            try {
                readBook.saveBook(library.getBooks(), filepath);
                refreshTable();
                showAlert("Success", "Book deleted successfully!");
            } catch (Exception e) {
                showAlert("Error", "Book deleted but failed to save to file: " + e.getMessage());
                refreshTable(); // Still refresh to show the deletion
            }
        }
    }
    
    private void refreshTable() {
        bookData.clear();
        if (library != null) {
            bookData.addAll(library.getBooks());
        }
        // Update stats
        if (totalBooksLabel != null) {
            totalBooksLabel.setText("Total Books: " + bookData.size());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}