package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.LoanRecord;
import model.BorrowHistoryDAO;
import model.Session;

import java.io.IOException;

public class ReturnBooksController {

    @FXML private TableView<LoanRecord> tableLoans;
    @FXML private TableColumn<LoanRecord, String> colTitle;
    @FXML private TableColumn<LoanRecord, String> colLoanDate;
    @FXML private TableColumn<LoanRecord, String> colStatus;
    @FXML private TableColumn<LoanRecord, Void> colAction;

    private ObservableList<LoanRecord> loanList;

    @FXML
    public void initialize() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colLoanDate.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadActiveLoans();
        addReturnButtonToTable();
    }

    private void loadActiveLoans() {
        int userId = Session.getCurrentUserId();
        loanList = FXCollections.observableArrayList(BorrowHistoryDAO.getActiveLoansByUser(userId));
        tableLoans.setItems(loanList);
    }

    private void addReturnButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<LoanRecord, Void>() {
            private final Button btnReturn = new Button("Kembalikan");

            {
                btnReturn.setOnAction(event -> {
                    LoanRecord loan = getTableView().getItems().get(getIndex());
                    returnBook(loan);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnReturn);
                }
            }
        });
    }

    private void returnBook(LoanRecord loan) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Pengembalian");
        confirm.setHeaderText("Kembalikan Buku");
        confirm.setContentText("Apakah Anda yakin ingin mengembalikan buku:\n" + loan.getBookTitle() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = BorrowHistoryDAO.returnBook(loan.getLoanId());
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Berhasil");
                    alert.setContentText("Buku berhasil dikembalikan.");
                    alert.showAndWait();
                    loadActiveLoans(); // reload tabel
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
            Parent root = FXMLLoader.load(getClass().getResource("/view/user_dashboard.fxml"));
            Stage stage = (Stage) tableLoans.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard User");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
