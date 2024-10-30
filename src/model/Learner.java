package model;

import java.util.Date;

public class Learner {
    private String id;
    private String LearnerFirstName;
    private String LearnerLastName;
    private String LearnerEmail;
    private String LearnerPhoneNumber;
    private Date LearnerDob; // Ngày sinh
    private int LearnerAge; // Tuổi
    private String gender; // Giới tính

    public Learner() {}

    public Learner(String id, String LearnerFirstName, String lastName, String email, String LearnerPhoneNumber, Date LearnerDob, int LearnerAge, String gender) {
        this.id = id;
        this.LearnerFirstName = LearnerFirstName;
        this.LearnerLastName = lastName;
        this.LearnerEmail = email;
        this.LearnerPhoneNumber = LearnerPhoneNumber;
        this.LearnerDob = LearnerDob;
        this.LearnerAge = LearnerAge;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLearnerFirstName() {
        return LearnerFirstName;
    }

    public void setLearnerFirstName(String learnerFirstName) {
        this.LearnerFirstName = learnerFirstName;
    }

    public String getLastName() {
        return LearnerLastName;
    }

    public void setLastName(String lastName) {
        this.LearnerLastName = lastName;
    }

    public String getEmail() {
        return LearnerEmail;
    }

    public void setEmail(String email) {
        this.LearnerEmail = email;
    }

    public String getLearnerPhoneNumber() {
        return LearnerPhoneNumber;
    }

    public void setLearnerPhoneNumber(String learnerPhoneNumber) {
        this.LearnerPhoneNumber = learnerPhoneNumber;
    }

    public Date getLearnerDob() {
        return LearnerDob;
    }

    public void setLearnerDob(Date learnerDob) {
        this.LearnerDob = learnerDob;
    }

    public int getLearnerAge() {
        return LearnerAge;
    }

    public void setLearnerAge(int learnerAge) {
        this.LearnerAge = learnerAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return String.format("""
                         +-----------------+----------------------+
                         | %-15s | %-20s |
                         | %-15s | %-20s |
                         | %-15s | %-20s |
                         | %-15s | %-20s |
                         | %-15s | %-20s |
                         | %-15s | %-20s |
                         +-----------------+----------------------+
                         """,
                "First Name", LearnerFirstName,
                "Last Name", LearnerLastName,
                "Email", LearnerEmail,
                "Phone Number", LearnerPhoneNumber,
                "Age", LearnerAge,
                "Date of Birth", LearnerDob
        );
    }
}
