package model;

public abstract class Person {
    protected int id;
    protected String username;
    protected String role;

    public Person() {}

    public Person(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
