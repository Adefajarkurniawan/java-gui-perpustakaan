package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class RegisterController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Label lblMessage;

    @FXML
    private Hyperlink linkLogin;

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            lblMessage.setText("Semua field harus diisi");
            return;
        }

        if (username.contains(" ") || password.contains(" ")) {
            lblMessage.setText("Username dan Password tidak boleh mengandung spasi");
            return;
        }

        if (!password.equals(confirmPassword)) {
            lblMessage.setText("Password dan konfirmasi tidak cocok");
            return;
        }

        boolean success = User.register(username, password);
        if (success) {
            lblMessage.setText("Registrasi berhasil, silakan login");
            lblMessage.getStyleClass().removeAll("error-label");
            if (!lblMessage.getStyleClass().contains("success-label")) {
                lblMessage.getStyleClass().add("success-label");
            }
        } else {
            lblMessage.setText("Username sudah dipakai atau input tidak valid");
            lblMessage.getStyleClass().removeAll("success-label");
            if (!lblMessage.getStyleClass().contains("error-label")) {
                lblMessage.getStyleClass().add("error-label");
            }
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene loginScene = new Scene(loginRoot);
            Stage stage = (Stage) linkLogin.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}