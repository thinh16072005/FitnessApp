package model;

import java.time.LocalDate;

public class Subscription {
    private String subscriptionId;
    private String learnerId;
    private String courseId;
    private LocalDate EnrollmentDate;
    private String platform;
    private String status;

    Course course = new Course();

    public Subscription() {}

    public Subscription(String subscriptionId, String learnerId, String courseId, LocalDate EnrollmentDate, String platform, String status) {
        this.subscriptionId = subscriptionId;
        this.learnerId = learnerId;
        this.courseId = courseId;
        this.EnrollmentDate = EnrollmentDate;
        this.platform = platform;
        this.status = status;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
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

    public LocalDate getEnrollmentDate() {
        return EnrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.EnrollmentDate = enrollmentDate;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        if (platform.equalsIgnoreCase("online") || platform.equalsIgnoreCase("offline")) {
            this.platform = platform;
        } else {
            throw new IllegalArgumentException("Platform must be either 'online' or 'offline'");
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
                "Subscription ID", subscriptionId,
                "Learner ID", learnerId,
                "Course ID", courseId,
                "Enrollment Date", EnrollmentDate,
                "Status", status
        );
    }
}
