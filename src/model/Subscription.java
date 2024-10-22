package model;

import repository.SubscriptionRepo;

import java.time.LocalDate;
import java.util.Date;

public class Subscription {
    private String subscriptionId;
    private String learnerId;
    private String courseId;
    private LocalDate EnrollmentDate;
    private String status;

    Course course = new Course();

    public Subscription() {}

    public Subscription(String subscriptionId, String learnerId, String courseId, LocalDate EnrollmentDate, String status) {
        this.subscriptionId = subscriptionId;
        this.learnerId = learnerId;
        this.courseId = courseId;
        this.EnrollmentDate = EnrollmentDate;
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

//    public String getStatus() {
//        LocalDate enrollmentDate = getEnrollmentDate();
//        LocalDate startDate = course.getStartDate().toLocalDate();
//        LocalDate endDate = course.getEndDate().toLocalDate();
//        LocalDate currentDate = LocalDate.now();
//
//        if (enrollmentDate.isBefore(startDate)) {
//            return "Inactive";
//        } else if (enrollmentDate.equals(startDate)) {
//            return "Active";
//        } else if (currentDate.isAfter(endDate)) {
//            return "Expired";
//        } else if (currentDate.equals(endDate)) {
//            return "Completed";
//        } else {
//            return "Active";
//        }
//        return status;
//    }

    public void setStatus(String status) {
        this.status = status;
    }
}
