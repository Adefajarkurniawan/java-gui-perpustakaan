package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Person {

      public User() {
        this.role = "user";
    }

    public User(int id, String username) {
        super(id, username, "user");
    }

    // Pendaftaran user (registrasi) tetap static
    public static boolean register(String username, String password) {
        if (username.contains(" ") || password.contains(" ")) {
            return false; // tolak input yang mengandung spasi
        }

        String sqlCheck = "SELECT * FROM users WHERE username = ?";
        String sqlInsert = "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(sqlCheck);
             PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {

            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false; // username sudah ada
            }

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



    // Ubah login agar mengembalikan objek Person (User atau Admin)
    public static Person login(String username, String password) {
        if (username.contains(" ") || password.contains(" ")) {
            return null;
        }

        String sql = "SELECT id, password, role FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                String role = rs.getString("role");
                int id = rs.getInt("id");
                String inputHash = HashUtil.hashPassword(password);

                if (inputHash != null && inputHash.equals(storedHash)) {
                    if ("admin".equalsIgnoreCase(role)) {
                        return new Admin(id, username);
                    } else {
                        return new User(id, username);
                    }
                }
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}