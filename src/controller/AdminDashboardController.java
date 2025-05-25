package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.Book;
import model.BookDAO;


public class AdminDashboardController {

    @FXML private TableView<Book> tableBooks;
    @FXML private TableColumn<Book, Integer> colId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colYear;
    @FXML private TableColumn<Book, String> colGenre;
    @FXML private TableColumn<Book, Void> colActions;

    @FXML private TextField txtSearch;

    private ObservableList<Book> bookList;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("yearPublished"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));

        loadBooks();
        addActionButtonsToTable();
    }

    private void loadBooks() {
        // Retrieve book data from DAO
        bookList = FXCollections.observableArrayList(BookDAO.getAllBooks());
        tableBooks.setItems(bookList);
    }

    private void addActionButtonsToTable() {
        // Implement action buttons (edit, delete) in the table
    }

    // Implement actions like adding books, searching, etc.
    @FXML
    private void showAddBookDialog() {
        // Create dialog for adding a new book
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Tambah Buku");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Tambah", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the form fields
        TextField txtTitle = new TextField();
        txtTitle.setPromptText("Judul Buku");
        TextField txtAuthor = new TextField();
        txtAuthor.setPromptText("Penulis");
        TextField txtYear = new TextField();
        txtYear.setPromptText("Tahun Terbit (1990 - 2025)");
        ComboBox<String> comboGenre = new ComboBox<>();
        comboGenre.getItems().addAll("Fantasi", "Science", "Petualangan", "Misteri", "Horor", "Romansa", "Drama");
        comboGenre.setPromptText("Pilih Genre");

        // Layout form
        VBox vbox = new VBox(10, txtTitle, txtAuthor, txtYear, comboGenre);
        dialog.getDialogPane().setContent(vbox);

       // Handle the button click
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String title = txtTitle.getText().trim();
                String author = txtAuthor.getText().trim();
                String year = txtYear.getText().trim();
                String genre = comboGenre.getValue();

                // Validasi
                if (title.isEmpty() || author.isEmpty() || year.isEmpty() || genre == null) {
                    showErrorAlert("Semua field harus diisi!");
                    return null;
                }

                // Validasi tahun terbit
                  // Validasi tahun terbit (1990 - 2025)
                try {
                    int yearInt = Integer.parseInt(year);
                    if (yearInt < 1990 || yearInt > 2025) {
                        showErrorAlert("Tahun terbit harus antara 1990 hingga 2025!");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    showErrorAlert("Tahun terbit harus berupa angka 4 digit!");
                    return null;
                }

                // Validasi nama buku tidak boleh sama
                if (BookDAO.isTitleExist(title)) {
                    showErrorAlert("Nama buku sudah ada!");
                    return null;
                }

                // Create a new book object with the data from the form
                Book newBook = new Book();
                newBook.setTitle(title);
                newBook.setAuthor(author);
                newBook.setYearPublished(Integer.parseInt(year));
                newBook.setGenre(genre);

                return newBook;
            }
            return null;
        });

        // Show the dialog and get the result
        dialog.showAndWait().ifPresent(book -> {
            if (BookDAO.addBook(book)) {
                loadBooks();  // Reload the table with updated list of books
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Buku berhasil ditambahkan!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Gagal menambah buku.");
                alert.showAndWait();
            }
        });
    }

    
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSearch() {
        // Implement book search functionality
    }
}
