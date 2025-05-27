    package controller;

    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import java.io.IOException;
    import javafx.collections.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.stage.Stage;
    import javafx.event.ActionEvent;
    import model.Book;
    import model.BookDAO;
    import model.BorrowHistoryDAO;
    import model.IBookOperations;


    public class AdminDashboardController {
        @FXML
        private VBox contentArea; // To dynamically load content



        @FXML private TableView<Book> tableBooks;
        @FXML private TableColumn<Book, Integer> colId;
        @FXML private TableColumn<Book, String> colTitle;
        @FXML private TableColumn<Book, String> colAuthor;
        @FXML private TableColumn<Book, Integer> colYear;
        @FXML private TableColumn<Book, String> colGenre;
        @FXML private TableColumn<Book, Integer> colStock;
        @FXML private TableColumn<Book, Void> colActions;

        @FXML private TextField txtSearch;

        private ObservableList<Book> bookList;
        private IBookOperations ibookOperations;

        @FXML
        public void initialize() {

            

            ibookOperations = new BookDAO();

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
            colYear.setCellValueFactory(new PropertyValueFactory<>("yearPublished"));
            colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
            colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

            loadBooks();
            addActionButtonsToTable();

            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterBooks(newValue);
            });
        }

        @FXML
        private void showKelolaBuku() {
            try {
                // Memuat tampilan Kelola Buku
                Parent root = FXMLLoader.load(getClass().getResource("/view/kelola_buku.fxml"));
                contentArea.getChildren().clear();  // Clear previous content
                contentArea.getChildren().add(root);  // Menambahkan konten baru
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        
        @FXML
        private void showStatistik() {
            try {
                // Memuat tampilan statistik.fxml
                Parent root = FXMLLoader.load(getClass().getResource("/view/statistik.fxml"));
                contentArea.getChildren().clear();  // Clear previous content
                contentArea.getChildren().add(root);  // Menambahkan konten baru
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        private void handleLogout() {
            // Proses logout dan pindah ke halaman login
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
                Stage stage = (Stage) contentArea.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Login Perpustakaan");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void loadBooks() {
            bookList = FXCollections.observableArrayList(BookDAO.getAllBooks());
            tableBooks.setItems(bookList);
        }

        private void addActionButtonsToTable() {
            colActions.setCellFactory(param -> new TableCell<Book, Void>() {
                private final Button btnEdit = new Button("Edit");
                private final Button btnDelete = new Button("Hapus");
                private final HBox hbox = new HBox(5);  

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

                    // Styling tombol, bisa ditambah juga di CSS
                    btnEdit.getStyleClass().add("btn-edit");
                    btnDelete.getStyleClass().add("btn-delete");
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
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

            tableBooks.setItems(filteredList); 
        }


        private void showDeleteConfirmationDialog(Book book) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Hapus Buku");
            alert.setHeaderText("Apakah Anda yakin ingin menghapus buku ini?");
            alert.setContentText("Judul Buku: " + book.getTitle());

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (ibookOperations.deleteBook(book.getId())) {
                        loadBooks();  
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
            Dialog<Book> dialog = new Dialog<>();
            dialog.setTitle("Edit Buku");

            ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            TextField txtTitle = new TextField(book.getTitle());
            txtTitle.setPromptText("Judul Buku");
            TextField txtAuthor = new TextField(book.getAuthor());
            txtAuthor.setPromptText("Penulis");
            TextField txtYear = new TextField(String.valueOf(book.getYearPublished()));
            txtYear.setPromptText("Tahun Terbit lebih dari 1990");
            ComboBox<String> comboGenre = new ComboBox<>();
            comboGenre.getItems().addAll("Fantasi", "Science", "Petualangan", "Misteri", "Horor", "Romansa", "Drama");
            comboGenre.setValue(book.getGenre());
            comboGenre.setPromptText("Pilih Genre");
            TextField txtStock = new TextField(String.valueOf(book.getStock()));  
            txtStock.setPromptText("Stok (angka)");

            VBox vbox = new VBox(10, txtTitle, txtAuthor, txtYear, comboGenre, txtStock);
            dialog.getDialogPane().setContent(vbox);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    String title = txtTitle.getText().trim();
                    String author = txtAuthor.getText().trim();
                    String year = txtYear.getText().trim();
                    String genre = comboGenre.getValue();
                    String stockStr = txtStock.getText().trim();
                    int stock;
                    try {
                        stock = Integer.parseInt(stockStr);
                        if (stock < 0) {
                            showErrorAlert("Stok tidak boleh negatif!");
                            return null;
                        }
                    } catch (NumberFormatException e) {
                        showErrorAlert("Stok harus berupa angka!");
                        return null;
                    }
                    
                    if (title.isEmpty() || author.isEmpty() || year.isEmpty() || genre == null) {
                        showErrorAlert("Semua field harus diisi!");
                        return null;
                    }

                    try {
                        int yearInt = Integer.parseInt(year);
                        if (yearInt < 1990) {
                            showErrorAlert("Tahun terbit harus lebih dari Tahun 1990");
                            return null;
                        }
                    } catch (NumberFormatException e) {
                        showErrorAlert("Tahun terbit harus berupa angka 4 digit!");
                        return null;
                    }

                if (BookDAO.isTitleExist(title, book.getId())) {
                        showErrorAlert("Nama buku sudah ada!");
                        return null;
                    }

                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setYearPublished(Integer.parseInt(year));
                    book.setGenre(genre);
                    book.setStock(stock);

                    return book;
                }
                return null;
            });

            dialog.showAndWait().ifPresent(updatedBook -> {
                if (ibookOperations.updateBook(updatedBook)) {
                    loadBooks();  
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

        @FXML
        private void showAddBookDialog() {
            Dialog<Book> dialog = new Dialog<>();
            dialog.setTitle("Tambah Buku");

            ButtonType addButtonType = new ButtonType("Tambah", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            TextField txtTitle = new TextField();
            txtTitle.setPromptText("Judul Buku");
            TextField txtAuthor = new TextField();
            txtAuthor.setPromptText("Penulis");
            TextField txtYear = new TextField();
            txtYear.setPromptText("Tahun Terbit lebih dari 1990");
            ComboBox<String> comboGenre = new ComboBox<>();
            comboGenre.getItems().addAll("Fantasi", "Science", "Petualangan", "Misteri", "Horor", "Romansa", "Drama");
            comboGenre.setPromptText("Pilih Genre");
            TextField txtStock = new TextField();
            txtStock.setPromptText("Stock");
            
            VBox vbox = new VBox(10, txtTitle, txtAuthor, txtYear, comboGenre, txtStock);
            dialog.getDialogPane().setContent(vbox);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    String title = txtTitle.getText().trim();
                    String author = txtAuthor.getText().trim();
                    String year = txtYear.getText().trim();
                    String genre = comboGenre.getValue();
                    String stockStr = txtStock.getText().trim();
                    int stock;
                    try {
                        stock = Integer.parseInt(stockStr);
                        if (stock < 0) {
                            showErrorAlert("Stok tidak boleh negatif!");
                            return null;
                        }
                    } catch (NumberFormatException e) {
                        showErrorAlert("Stok harus berupa angka!");
                        return null;
                    }

                    // Validasi
                    if (title.isEmpty() || author.isEmpty() || year.isEmpty() || genre == null) {
                        showErrorAlert("Semua field harus diisi!");
                        return null;
                    }

                    try {
                        int yearInt = Integer.parseInt(year);
                        if (yearInt < 1990) {
                            showErrorAlert("Tahun terbit harus lebih dari Tahun 1990!");
                            return null;
                        }
                    } catch (NumberFormatException e) {
                        showErrorAlert("Tahun terbit harus berupa angka 4 digit!");
                        return null;
                    }

                    // Validasi nama buku tidak boleh sama
                    if (BookDAO.isTitleExist(title, 0)) {
                        showErrorAlert("Nama buku sudah ada!");
                        return null;
                    }

                    // Create a new book object with the data from the form
                    Book newBook = new Book();
                    newBook.setTitle(title);
                    newBook.setAuthor(author);
                    newBook.setYearPublished(Integer.parseInt(year));
                    newBook.setGenre(genre);
                    newBook.setStock(stock);
                    return newBook;
                }
                return null;
            });

            // Show the dialog and get the result
            dialog.showAndWait().ifPresent(book -> {
                if (ibookOperations.addBook(book)) {
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
        private void openUsersLoans() {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/admin_users_loans.fxml"));
                Stage stage = (Stage) tableBooks.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("User dan Peminjaman");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        
    

        @FXML
        private void handleSearch() {
            // Implement book search functionality
        }
    }
