package model;

public class Admin extends Person {

    public Admin() {
        this.role = "admin";
    }

    public Admin(int id, String username) {
        super(id, username, "admin");
    }

    // Bisa tambahkan fungsi khusus Admin di sini bila diperlukan
}
