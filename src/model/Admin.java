package model;

public class Admin extends Person {

    public Admin() {
        this.role = "admin";
    }

    public Admin(int id, String username) {
        super(id, username, "admin");
    }

    @Override
    public String getDashboardView() {
        return "/view/admin_dashboard.fxml";  // override supaya admin buka dashboard admin
    }
}
