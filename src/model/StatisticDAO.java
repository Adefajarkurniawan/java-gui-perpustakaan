package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticDAO {

    // Method untuk mendapatkan total buku
    public int getTotalBooks() {
        String query = "SELECT COUNT(*) AS total_books FROM books";  // Ganti dengan nama tabel buku di database Anda
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total_books");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;  // Jika terjadi error, return 0
    }

    // Method untuk mendapatkan total pengguna
    public int getTotalUsers() {
        String query = "SELECT COUNT(*) AS total_users FROM users";  // Ganti dengan nama tabel pengguna di database Anda
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total_users");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;  // Jika terjadi error, return 0
    }

    // Method untuk mendapatkan jumlah buku yang dipinjam
    public int getLoanedBooks() {
        String query = "SELECT COUNT(*) AS loaned_books FROM loans WHERE return_date IS NULL";  // Ganti dengan nama tabel pinjaman di database Anda
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("loaned_books");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;  // Jika terjadi error, return 0
    }
}
