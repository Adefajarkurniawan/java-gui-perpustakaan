package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.BorrowHistory;
import model.BorrowHistoryDAO;
import model.Session;

import java.io.IOException;

public class BorrowHistoryController {

    @FXML private TableView<BorrowHistory> tableHistory;
    @FXML private TableColumn<BorrowHistory, String> colTitle;
    @FXML private TableColumn<BorrowHistory, String> colLoanDate;
    @FXML private TableColumn<BorrowHistory, String> colReturnDate;
    @FXML private TableColumn<BorrowHistory, String> colStatus;

    private ObservableList<BorrowHistory> historyList;

    @FXML
    public void initialize() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colLoanDate.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadBorrowHistory();
    }

    private void loadBorrowHistory() {
        int userId = Session.getCurrentUserId();
        historyList = FXCollections.observableArrayList(BorrowHistoryDAO.getUserBorrowHistory(userId));
        tableHistory.setItems(historyList);
    }

    @FXML
    private void goBackToDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/user_dashboard.fxml"));
            Stage stage = (Stage) tableHistory.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard User");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
