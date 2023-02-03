package cs.dawson.dawsonelectriccurrents.beans;

import android.text.Html;

import java.io.Serializable;

/**
 * This is the teacher bean.  Contains getters and setters.
 * @author  Kevin
 * @version 1.0
 */

public class Teacher implements Serializable {
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String office;
    private String local;
    private String position;
    private String department;
    private String sector;

    /**
     * Default constructor
     */
    public Teacher() {
        this.firstName = "";
        this.lastName = "";
        this.fullName = "";
        this.email = "";
        this.office = "";
        this.local = "";
        this.position = "";
        this.department = "";
        this.sector = "";
    }

    /**
     * Constructor with parameters
     * @param fn
     * @param ln
     * @param email
     * @param office
     * @param local
     * @param position
     * @param department
     * @param sector
     */
    public Teacher(String fn, String ln, String fullN, String email, String office,
                   String local, String position, String department, String sector) {
        this.firstName = fn;
        this.lastName = ln;
        this.fullName = fullN;
        this.email = email;
        this.office = office;
        this.local = local;
        this.position = position;
        this.department = department;
        this.sector = sector;
    }

    // Getters and setters

    public String getFirstName() { return Html.fromHtml(firstName).toString(); }

    public void setFirstName(String fn) { this.firstName = fn; }

    public String getLastName() { return Html.fromHtml(lastName).toString(); }

    public void setLastName(String ln) { this.lastName = ln; }

    public String getFullName() { return Html.fromHtml(fullName).toString(); }

    public void setFullName(String fn) { this.fullName = fn; }

    public String getEmail() { return Html.fromHtml(email).toString(); }

    public void setEmail(String email) { this.email = email; }

    public String getOffice() { return office; }

    public void setOffice(String office) { this.office = office; }

    public String getLocal() { return local; }

    public void setLocal(String local) { this.local = local; }

    public String getPosition() { return position; }

    public void setPosition(String position) { this.position = position; }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }

    public String getSector() { return sector; }

    public void setSector(String sector) { this.sector = sector; }

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
        Teacher other = (Teacher) obj;
        if (!this.getFirstName().equals(other.getFirstName())) {
            return false;
        }
        if (!this.getLastName().equals(other.getLastName())) {
            return false;
        }
        if (!this.getFirstName().equals(other.getFirstName())) {
            return false;
        }
        if (!this.getEmail().equals(other.getEmail())) {
            return false;
        }
        if (!this.getOffice().equals(other.getOffice())) {
            return false;
        }
        if (!this.getLocal().equals(other.getLocal())) {
            return false;
        }
        if (!this.getPosition().equals(other.getPosition())) {
            return false;
        }
        if (!this.getDepartment().equals(other.getDepartment())) {
            return false;
        }
        if (!this.getSector().equals(other.getSector())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Teacher{First name: " + firstName + ", Last name: " + lastName +
                "Full name: " + fullName + ", Email: " + email +
                ", Office: " + office + ", Local: " + local + ", Position: " + position + ", Department: " +
                department + ", Sector: " + sector + "}";
    }

}
