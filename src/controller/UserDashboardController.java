package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Book;
import model.BookDAO;
import model.BorrowHistoryDAO;
import model.Session;

public class UserDashboardController {

    @FXML private TableView<Book> tableBooks;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colYear;
    @FXML private TableColumn<Book, String> colGenre;
    @FXML private TableColumn<Book, Integer> colStock;
    @FXML private TableColumn<Book, Void> colAction;  // Tambahan kolom aksi

    @FXML private TextField txtSearch;
    

    private ObservableList<Book> bookList;

    @FXML
    public void initialize() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("yearPublished"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));  // Tambah ini

        loadBooks();
        addActionButtonsToTable();

        // Add listener to search field for live filtering
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterBooks(newValue);
        });
    }

    private void loadBooks() {
        bookList = FXCollections.observableArrayList(BookDAO.getAllBooks());
        tableBooks.setItems(bookList);
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



     @FXML
    private void handleSearch() {
        // Implement book search functionality
    }


    private void addActionButtonsToTable() {
    colAction.setCellFactory(param -> new TableCell<Book, Void>() {
        private final Button btnBorrow = new Button("Pinjam");
        private final Button btnReturn = new Button("Kembalikan");

        {
            btnBorrow.setOnAction(event -> {
                Book book = getTableView().getItems().get(getIndex());
                borrowBook(book);
            });

            btnReturn.setOnAction(event -> {
                Book book = getTableView().getItems().get(getIndex());
                returnBook(book);
            });
        }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                Book book = getTableView().getItems().get(getIndex());
                int userId = Session.getCurrentUserId();
                boolean isBorrowed = BorrowHistoryDAO.isBookBorrowedByUser(userId, book.getId());

                if (isBorrowed) {
                    setGraphic(btnReturn);
                } else {
                    setGraphic(btnBorrow);
                }
            }
        });
    }


        private void returnBook(Book book) {
        int userId = Session.getCurrentUserId();
        if (userId == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("User belum login!");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Pengembalian");
        confirm.setHeaderText("Kembalikan Buku");
        confirm.setContentText("Apakah Anda yakin ingin mengembalikan buku:\n" + book.getTitle() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = BorrowHistoryDAO.returnBookByUserAndBook(userId, book.getId());
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Berhasil");
                    alert.setContentText("Buku berhasil dikembalikan.");
                    alert.showAndWait();
                    loadBooks(); // refresh tabel
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gagal");
                    alert.setContentText("Gagal mengembalikan buku.");
                    alert.showAndWait();

                }
            }
        });
    }





    private void borrowBook(Book book) {
        int userId = Session.getCurrentUserId();

        if (userId == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("User belum login!");
            alert.showAndWait();
            return;
        }

        // Cek stok buku sebelum pinjam
        if (book.getStock() <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stok Kosong");
            alert.setContentText("Maaf, stok buku ini sudah habis.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Peminjaman");
        confirm.setHeaderText("Pinjam Buku");
        confirm.setContentText("Apakah Anda yakin ingin meminjam buku:\n" + book.getTitle() + "?");
    
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = BorrowHistoryDAO.borrowBook(userId, book);

               if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Berhasil");
                    alert.setContentText("Buku berhasil dipinjam.");
                    alert.showAndWait();
                    loadBooks();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gagal");
                    alert.setContentText("Anda sudah meminjam buku ini dan belum mengembalikannya.");
                    alert.showAndWait();
                }
            }
        });

        if (book.getStock() <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stok Kosong");
            alert.setContentText("Maaf, stok buku ini sudah habis.");
            alert.showAndWait();
            return;
        }
    }


    @FXML
    private void openBorrowHistory() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/borrow_history.fxml"));
            Stage stage = (Stage) tableBooks.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Riwayat Peminjaman");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleLogout() {
        model.Session.clear();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Stage stage = (Stage) tableBooks.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Perpustakaan");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
