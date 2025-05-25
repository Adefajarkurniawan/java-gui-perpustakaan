package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    

    public static boolean register(String username, String password) {
        if (username.contains(" ") || password.contains(" ")) {
            return false; // tolak input yang mengandung spasi
        }

        String sqlCheck = "SELECT * FROM users WHERE username = ?";
        String sqlInsert = "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(sqlCheck);
             PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {

            // cek username sudah ada atau belum
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false; // username sudah ada
            }

            // Hash password dengan SHA-256
            String hashedPassword = HashUtil.hashPassword(password);

            insertStmt.setString(1, username);
            insertStmt.setString(2, hashedPassword);
            int rows = insertStmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static String loginAndGetRole(String username, String password) {
        if (username.contains(" ") || password.contains(" ")) {
            return null;
        }

        String sql = "SELECT password, role FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                String role = rs.getString("role");
                String inputHash = HashUtil.hashPassword(password);

                if (inputHash != null && inputHash.equals(storedHash)) {
                    return role; // kembalikan role jika login sukses
                }
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


     // Ambil user id dari username
    public static int getUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // jika user tidak ditemukan
    }
}