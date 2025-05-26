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
import model.Person;
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

        Person person = User.login(username, password); // sekarang kembalikan objek Person
        if (person != null) {
            Session.setCurrentUser(person.getUsername());
            Session.setCurrentUserId(person.getId());

            lblMessage.setText("Login berhasil sebagai " + person.getRole());

            try {
                Parent root;
                if ("admin".equalsIgnoreCase(person.getRole())) {
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
        // Pastikan linkRegister sudah terinisialisasi dengan benar
        if (linkRegister != null) {
            try {
                Parent registerRoot = FXMLLoader.load(getClass().getResource("/view/register.fxml"));
                Scene registerScene = new Scene(registerRoot);
                Stage stage = (Stage) linkRegister.getScene().getWindow();
                stage.setScene(registerScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                lblMessage.setText("Gagal membuka halaman registrasi. Silakan coba lagi.");
            }
        } else {
            System.err.println("linkRegister is null.");
        }
    }
}