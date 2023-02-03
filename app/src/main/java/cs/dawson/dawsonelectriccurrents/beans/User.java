package cs.dawson.dawsonelectriccurrents.beans;

import java.io.Serializable;

/**
 * This is the user bean.  Contains getters and setters.
 * @author Kevin
 * @version 1.0
 */

public class User implements Serializable {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String lastUpdated;

    /**
     * Default constructor
     */
    public User() {
        this.userId = 0;
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.password = "";
        this.lastUpdated = "";
    }

    /**
     * Constructor with parameters
     * @param userId
     * @param fName
     * @param lName
     * @param email
     * @param password
     * @param lastUpdated
     */
    public User(int userId, String fName, String lName, String email, String password, String lastUpdated) {
        this.userId = userId;
        this.firstName = fName;
        this.lastName = lName;
        this.email = email;
        this.password = password;
        this.lastUpdated = lastUpdated;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int uId) {
        this.userId = uId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fn) {
        this.firstName = fn;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String ln) {
        this.lastName = ln;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lu) {
        this.lastUpdated = lu;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (this.getUserId() != other.getUserId()) {
            return false;
        }
        if (!this.getFirstName().equals(other.getFirstName())) {
            return false;
        }
        if (!this.getLastName().equals(other.getLastName())) {
            return false;
        }
        if (!this.getEmail().equals(other.getEmail())) {
            return false;
        }
        if (!this.getPassword().equals(other.getPassword())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{ID: " + userId + ", Firstname: " + firstName + ", Lastname: " + lastName +
                ", Email: " + email + ", Password: " + password + ", Last Updated: " + lastUpdated + "}";
    }
}
