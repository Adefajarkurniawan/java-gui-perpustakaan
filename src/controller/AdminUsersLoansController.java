package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.collections.*;
import model.UserLoanRecord;
import model.BorrowHistoryDAO;

public class AdminUsersLoansController {

    @FXML private TableView<UserLoanRecord> tableUsersLoans;
    @FXML private TableColumn<UserLoanRecord, String> colUsername;
    @FXML private TableColumn<UserLoanRecord, String> colBookTitle;
    @FXML private TableColumn<UserLoanRecord, String> colLoanDate;
    @FXML private TableColumn<UserLoanRecord, String> colStatus;
    @FXML private TableColumn<UserLoanRecord, Void> colAction;

    private ObservableList<UserLoanRecord> loanList;

    @FXML
    public void initialize() {
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colLoanDate.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadUserLoans();
        addReturnButtonToTable();
    }

    private void loadUserLoans() {
        loanList = FXCollections.observableArrayList(BorrowHistoryDAO.getAllUserLoans());
        tableUsersLoans.setItems(loanList);
    }

    private void addReturnButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<UserLoanRecord, Void>() {
            private final Button btnReturn = new Button("Kembalikan");

            {
                btnReturn.getStyleClass().add("btn-return");
                
                btnReturn.setOnAction(event -> {
                    UserLoanRecord loan = getTableView().getItems().get(getIndex());
                    returnBook(loan);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                UserLoanRecord loan = getTableView().getItems().get(getIndex());
                // Tampilkan tombol hanya jika status masih 'dipinjam'
                if ("dipinjam".equalsIgnoreCase(loan.getStatus())) {
                    setGraphic(btnReturn);
                } else {
                    setGraphic(null);
                }
            }
        });
    }

    private void returnBook(UserLoanRecord loan) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Pengembalian");
        confirm.setHeaderText("Kembalikan Buku");
        confirm.setContentText("Apakah Anda yakin ingin mengembalikan buku:\n" + loan.getBookTitle() + " oleh user " + loan.getUsername() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = BorrowHistoryDAO.returnBookByLoanId(loan.getLoanId());
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Berhasil");
                    alert.setContentText("Buku berhasil dikembalikan.");
                    alert.showAndWait();
                    loadUserLoans(); // reload data
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gagal");
                    alert.setContentText("Gagal mengembalikan buku.");
                    alert.showAndWait();
                }
            }
        });
    }

    @FXML
    private void goBackToDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/admin_dashboard.fxml"));
            Stage stage = (Stage) tableUsersLoans.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard Admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
}
