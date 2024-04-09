package ua.kiev.prog.model;

public enum UserRole {
    ADMIN;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
