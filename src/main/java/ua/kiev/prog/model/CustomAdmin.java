package ua.kiev.prog.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CustomAdmin {
    @Id
    @GeneratedValue
    private Long id;
    private final String ROLE = "ADMIN";
    private String login;
    private String password;

    public CustomAdmin() {
    }

    public CustomAdmin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getROLE() {
        return ROLE;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
