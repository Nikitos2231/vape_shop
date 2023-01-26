package com.example.vape_shop.services;

import com.example.vape_shop.models.Feedback;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ManService manService;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository, ManService manService) {
        this.feedbackRepository = feedbackRepository;
        this.manService = manService;
    }

    @Transactional
    public void saveFeedback(Feedback feedback, Man manWhoAddFeedback, Man manWhoGetFeedback) {
        addRelationBetweenFeedbackAndManWhoAddIt(manWhoAddFeedback, feedback);
        addRelationBetweenFeedbackAndManWhoGetIt(manWhoGetFeedback, feedback);
        feedback.setManToFeedback(manWhoGetFeedback);
        feedback.setManFromFeedback(manWhoAddFeedback);
        feedbackRepository.save(feedback);
        updateRatingForMan(manWhoGetFeedback);
    }

    private void addRelationBetweenFeedbackAndManWhoAddIt(Man manWhoAddFeedback, Feedback feedbackThatAddMan) {
        HashSet<Feedback> manWhoAddFeedbackSet = new HashSet<>(manWhoAddFeedback.getManToFeedback());
        manWhoAddFeedbackSet.add(feedbackThatAddMan);
        manWhoAddFeedback.setManToFeedback(manWhoAddFeedbackSet);
        manService.saveMan(manWhoAddFeedback);
    }

    private void addRelationBetweenFeedbackAndManWhoGetIt(Man manWhoGetFeedback, Feedback feedbackThatAddMan) {
        HashSet<Feedback> manWhoGetFeedbackSet = new HashSet<>(manWhoGetFeedback.getManFromFeedback());
        manWhoGetFeedbackSet.add(feedbackThatAddMan);
        manWhoGetFeedback.setManFromFeedback(manWhoGetFeedbackSet);
        manService.saveMan(manWhoGetFeedback);
    }

    private void updateRatingForMan(Man man) {
        Man manWhoGetFeedback = manService.findOne(man.getUserId());
        long oldRating = manWhoGetFeedback.getUserCountStars();
        List<Feedback> feedbacksForMan = getFeedbacksForMan(manWhoGetFeedback);
        double amountFeedbacksForMan = feedbacksForMan.size();
        double sumRating = 0;
        for (Feedback feedback : feedbacksForMan) {
            sumRating += feedback.getFeedbackRating();
        }
        if (sumRating != 0) {
            long newRating = Math.round(sumRating / amountFeedbacksForMan);
            if (newRating != oldRating) {
                manWhoGetFeedback.setUserCountStars((int) newRating);
                manService.saveMan(manWhoGetFeedback);
            }
        }
    }

    public List<Feedback> getFeedbacksForMan(Man manWhoGetFeedback) {
       return feedbackRepository.getFeedbackByManToFeedback(manWhoGetFeedback);
    }

    public List<Feedback> findAllFeedbacks() {
        return feedbackRepository.findAll();
    }
}
