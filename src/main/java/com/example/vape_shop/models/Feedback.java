package com.example.vape_shop.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private int feedbackId;

    @Column(name = "rating")
    private int feedbackRating;

    @Column(name = "feedback_text")
    @Size(min = 5, max = 300, message = "В отзыве должно быть от 5 до 300 символов!")
    private String feedbackText;

    @ManyToOne()
    @JoinColumn(name = "man_to_id", referencedColumnName = "user_id")
    private Man manToFeedback;

    @ManyToOne()
    @JoinColumn(name = "man_from_id", referencedColumnName = "user_id")
    private Man manFromFeedback;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "feedback_date_of_create")
    private Date feedbackDateOfCreate = new Date();

    public Feedback() {
    }

    public Feedback(int feedbackId, int feedbackRating) {
        this.feedbackId = feedbackId;
        this.feedbackRating = feedbackRating;
    }

    public Date getFeedbackDateOfCreate() {
        return feedbackDateOfCreate;
    }

    public void setFeedbackDateOfCreate(Date feedbackDateOfCreate) {
        this.feedbackDateOfCreate = feedbackDateOfCreate;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(int feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public Man getManToFeedback() {
        return manToFeedback;
    }

    public void setManToFeedback(Man manToFeedback) {
        this.manToFeedback = manToFeedback;
    }

    public Man getManFromFeedback() {
        return manFromFeedback;
    }

    public void setManFromFeedback(Man manFromFeedback) {
        this.manFromFeedback = manFromFeedback;
    }
}
