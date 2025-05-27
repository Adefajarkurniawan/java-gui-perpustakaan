package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.StatisticDAO;

public class StatistikController {

    @FXML private Label totalBooks;
    @FXML private Label totalUsers;
    @FXML private Label loanedBooks;

    @FXML
    public void initialize() {
        // Mengambil data statistik dari model atau database
        StatisticDAO statisticDAO = new StatisticDAO();
        int totalBooksCount = statisticDAO.getTotalBooks();
        int totalUsersCount = statisticDAO.getTotalUsers();
        int loanedBooksCount = statisticDAO.getLoanedBooks();

        // Menampilkan data statistik ke dalam Label
        totalBooks.setText(String.valueOf(totalBooksCount));
        totalUsers.setText(String.valueOf(totalUsersCount));
        loanedBooks.setText(String.valueOf(loanedBooksCount));
    }
}
