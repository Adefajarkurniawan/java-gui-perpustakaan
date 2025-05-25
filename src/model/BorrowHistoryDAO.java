package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowHistoryDAO {

    // Simpan peminjaman buku dengan user ID (int)
    public static boolean borrowBook(int userId, Book book) {
        if (hasActiveLoan(userId, book.getId())) {
            // Sudah ada pinjaman aktif, tolak
            return false;
        }
        
        String sql = "INSERT INTO loans (user_id, book_id, loan_date, return_date, status) VALUES (?, ?, ?, NULL, 'dipinjam')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, book.getId());
            stmt.setDate(3, Date.valueOf(LocalDate.now()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static List<BorrowHistory> getUserBorrowHistory(int userId) {
        List<BorrowHistory> list = new ArrayList<>();
        String sql = "SELECT b.title, l.loan_date, l.return_date, l.status " +
                     "FROM loans l " +
                     "JOIN books b ON l.book_id = b.id " +
                     "WHERE l.user_id = ? " +
                     "ORDER BY l.loan_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                Date loanDate = rs.getDate("loan_date");
                Date returnDate = rs.getDate("return_date");
                String status = rs.getString("status");

                list.add(new BorrowHistory(
                    title,
                    loanDate != null ? loanDate.toString() : "-",
                    returnDate != null ? returnDate.toString() : "-",
                    status
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }



    public static boolean hasActiveLoan(int userId, int bookId) {
    String sql = "SELECT COUNT(*) FROM loans WHERE user_id = ? AND book_id = ? AND status = 'dipinjam'";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, userId);
        stmt.setInt(2, bookId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;  // true jika ada peminjaman aktif
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

}
