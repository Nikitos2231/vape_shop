package com.example.vape_shop.repositories;

import com.example.vape_shop.models.Feedback;
import com.example.vape_shop.models.Man;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> getFeedbackByManToFeedback(Man man);

    List<Feedback> getFeedbackByManFromFeedback(Man man);
}
