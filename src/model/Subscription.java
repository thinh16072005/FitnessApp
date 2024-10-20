package model;

public class Subscription {
    private String subscriptionId;
    private String learnerId;
    private String courseId;
    private String EnrollmentDate;
    private char status;

    public Subscription() {}

    public Subscription(String subsciptionId, String learnerId, String courseId, String EnrollmentDate, char status) {
        this.subscriptionId = subsciptionId;
        this.learnerId = learnerId;
        this.courseId = courseId;
        this.EnrollmentDate = EnrollmentDate;
        this.status = status;
    }

    public String getSubsciptionId() {
        return subscriptionId;
    }

    public void setSubsciptionId(String subsciptionId) {
        this.subscriptionId = subsciptionId;
    }

    public String getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(String learnerId) {
        this.learnerId = learnerId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getEnrollmentDate() {
        return EnrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        EnrollmentDate = enrollmentDate;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
