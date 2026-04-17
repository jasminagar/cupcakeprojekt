package entities;

public class User {
private int id;
private String email;
private String name;
private String password;
private double balance;
private String role;

    public User(String name, String email, String password, double balance, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.role = role;
    }

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getRole() {
        return role;
    }

}
