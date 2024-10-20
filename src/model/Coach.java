package model;

public class Coach {
    private String coachId;
    private String coachFirstName;
    private String coachLastName;
    private String coachEmail;
    private String coachPhoneNumber;

    public Coach() {}

    public Coach(String coachId, String coachFirstName, String coachLastName, String coachEmail, String coachPhoneNumber) {
        this.coachId = coachId;
        this.coachFirstName = coachFirstName;
        this.coachLastName = coachLastName;
        this.coachEmail = coachEmail;
        this.coachPhoneNumber = coachPhoneNumber;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getCoachFirstName() {
        return coachFirstName;
    }

    public void setCoachFirstName(String coachFirstName) {
        this.coachFirstName = coachFirstName;
    }

    public String getCoachLastName() {
        return coachLastName;
    }

    public void setCoachLastName(String coachLastName) {
        this.coachLastName = coachLastName;
    }

    public String getCoachEmail() {
        return coachEmail;
    }

    public void setCoachEmail(String coachEmail) {
        this.coachEmail = coachEmail;
    }

    public String getCoachPhoneNumber() {
        return coachPhoneNumber;
    }

    public void setCoachPhoneNumber(String coachPhoneNumber) {
        this.coachPhoneNumber = coachPhoneNumber;
    }

    @Override
    public String toString() {
        return String.format("""
                         +-----------------+----------------------+
                         | %-15s | %-20s |
                         +-----------------+----------------------+
                         | %-15s | %-20s |
                         | %-15s | %-20s |
                         | %-15s | %-20s |
                         | %-15s | %-20s |
                         +-----------------+----------------------+
                         """,
                         "Coach ID", coachId,
                         "First Name", coachFirstName,
                         "Last Name", coachLastName,
                         "Email", coachEmail,
                         "Phone Number", coachPhoneNumber);
    }
}
