package Votingsystem;

public abstract class AbstractAdministrator {
    protected String adminId;
    protected String name;
    protected String role;
    protected boolean isActive;

    public AbstractAdministrator(String adminId, String name, String role) {
        this.adminId = adminId;
        this.name = name;
        this.role = role;
        this.isActive = true;
    }

    public String getAdminId() {
        return adminId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public abstract boolean hasPermission(String operation);
}

