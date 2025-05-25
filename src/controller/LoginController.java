package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.Session;


public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;

    @FXML
    private Hyperlink linkRegister;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Username dan Password harus diisi");
            return;
        }

        String role = User.loginAndGetRole(username, password);
        if (role != null) {
            // Set session info
            Session.setCurrentUser(username);
            int userId = User.getUserIdByUsername(username);
            Session.setCurrentUserId(userId);


            lblMessage.setText("Login berhasil sebagai " + role);

            
            try {
                Parent root;
                if (role.equals("admin")) {
                    root = FXMLLoader.load(getClass().getResource("/view/admin_dashboard.fxml"));
                } else {
                    root = FXMLLoader.load(getClass().getResource("/view/user_dashboard.fxml"));
                }
                Stage stage = (Stage) lblMessage.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                lblMessage.setText("Gagal membuka halaman dashboard.");
            }
        } else {
            lblMessage.setText("Username atau Password salah");
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        try {
            Parent registerRoot = FXMLLoader.load(getClass().getResource("/view/register.fxml"));
            Scene registerScene = new Scene(registerRoot);
            Stage stage = (Stage) linkRegister.getScene().getWindow();
            stage.setScene(registerScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}