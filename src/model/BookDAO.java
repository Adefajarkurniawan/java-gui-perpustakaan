package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setYearPublished(rs.getInt("year_published"));
                book.setGenre(rs.getString("genre"));
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public static boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, year_published, genre) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getYearPublished());
            stmt.setString(4, book.getGenre());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cek apakah nama buku sudah ada di database
    public static boolean isTitleExist(String title) {
        String sql = "SELECT COUNT(*) FROM books WHERE title = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Buku dengan nama ini sudah ada
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Nama buku belum ada
    }

    public static boolean updateBook(Book book) {
        String sql = "UPDATE books SET title=?, author=?, year_published=?, genre=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getYearPublished());
            stmt.setString(4, book.getGenre());
            stmt.setInt(5, book.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
