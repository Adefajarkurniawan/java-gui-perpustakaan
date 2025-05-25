package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
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

        // Add listener to search field for live filtering
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterBooks(newValue);
        });
    }

    private void loadBooks() {
        // Retrieve book data from DAO
        bookList = FXCollections.observableArrayList(BookDAO.getAllBooks());
        tableBooks.setItems(bookList);
    }

    private void addActionButtonsToTable() {
        // Menambahkan tombol Edit dan Hapus di kolom Aksi
        colActions.setCellFactory(param -> new TableCell<Book, Void>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnDelete = new Button("Hapus");

            {
                // Action untuk tombol Edit
                btnEdit.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showEditBookDialog(book);
                });

                // Action untuk tombol Hapus
                btnDelete.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showDeleteConfirmationDialog(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Buat HBox untuk menampung tombol Edit dan Hapus
                    HBox hbox = new HBox(10, btnEdit, btnDelete);
                    setGraphic(hbox);
                }
            }
        });
    }

     private void filterBooks(String query) {
        ObservableList<Book> filteredList = FXCollections.observableArrayList();

        for (Book book : BookDAO.getAllBooks()) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) || 
                book.getAuthor().toLowerCase().contains(query.toLowerCase()) || 
                book.getGenre().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(book);
            }
        }

        tableBooks.setItems(filteredList); // Set the filtered list to the table
    }


    // Dialog to confirm delete action
    private void showDeleteConfirmationDialog(Book book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus Buku");
        alert.setHeaderText("Apakah Anda yakin ingin menghapus buku ini?");
        alert.setContentText("Judul Buku: " + book.getTitle());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Proceed with delete
                if (BookDAO.deleteBook(book.getId())) {
                    loadBooks();  // Reload the table with updated list of books
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setContentText("Buku berhasil dihapus!");
                    successAlert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setContentText("Gagal menghapus buku.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    @FXML
    private void showEditBookDialog(Book book) {
        // Create dialog for editing the book
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Edit Buku");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the form fields
        TextField txtTitle = new TextField(book.getTitle());
        txtTitle.setPromptText("Judul Buku");
        TextField txtAuthor = new TextField(book.getAuthor());
        txtAuthor.setPromptText("Penulis");
        TextField txtYear = new TextField(String.valueOf(book.getYearPublished()));
        txtYear.setPromptText("Tahun Terbit (1990 - 2025)");
        ComboBox<String> comboGenre = new ComboBox<>();
        comboGenre.getItems().addAll("Fantasi", "Science", "Petualangan", "Misteri", "Horor", "Romansa", "Drama");
        comboGenre.setValue(book.getGenre());
        comboGenre.setPromptText("Pilih Genre");

        // Layout form
        VBox vbox = new VBox(10, txtTitle, txtAuthor, txtYear, comboGenre);
        dialog.getDialogPane().setContent(vbox);

        // Handle the button click
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String title = txtTitle.getText().trim();
                String author = txtAuthor.getText().trim();
                String year = txtYear.getText().trim();
                String genre = comboGenre.getValue();

                // Validasi
                if (title.isEmpty() || author.isEmpty() || year.isEmpty() || genre == null) {
                    showErrorAlert("Semua field harus diisi!");
                    return null;
                }

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

                // Update the book data
                book.setTitle(title);
                book.setAuthor(author);
                book.setYearPublished(Integer.parseInt(year));
                book.setGenre(genre);

                return book;
            }
            return null;
        });

        // Show the dialog and get the result
        dialog.showAndWait().ifPresent(updatedBook -> {
            if (BookDAO.updateBook(updatedBook)) {
                loadBooks();  // Reload the table with updated list of books
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Buku berhasil diperbarui!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Gagal memperbarui buku.");
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

    
  

    @FXML
    private void handleSearch() {
        // Implement book search functionality
    }
}
