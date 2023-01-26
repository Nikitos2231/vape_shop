package com.example.vape_shop.services;

import com.example.vape_shop.models.Feedback;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.repositories.FeedbackRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith({MockitoExtension.class})
class FeedbackServiceTest {

    @Autowired
    private FeedbackService feedbackService;
    @MockBean
    private FeedbackRepository feedbackRepository;
    @MockBean
    private ManService manService;

    @Test
    void saveFeedback() {
        Feedback feedback = new Feedback(3, 5);
        Man manWhoAddFeedback = new Man();
        Man manWhoGetFeedback = new Man();

        HashSet<Feedback> feedbacks = new HashSet<>();
        Feedback feedback2 = new Feedback(1, 3);
        Feedback feedback3 = new Feedback(2, 4);
        feedbacks.add(feedback2);
        feedbacks.add(feedback3);

        Set<Feedback> expectedSetForManWhoAddFeedback = new HashSet<>();
        expectedSetForManWhoAddFeedback.add(feedback2);
        expectedSetForManWhoAddFeedback.add(feedback3);
        expectedSetForManWhoAddFeedback.add(feedback);

        manWhoAddFeedback.setManToFeedback(feedbacks);

        manWhoGetFeedback.setManFromFeedback(feedbacks);
        manWhoGetFeedback.setUserCountStars(3);

        Mockito.doReturn(manWhoGetFeedback).when(manService).findOne(Mockito.anyInt());
        Mockito.doReturn(expectedSetForManWhoAddFeedback.stream().toList()).when(feedbackRepository).getFeedbackByManToFeedback(manWhoGetFeedback);
        Mockito.doReturn(null).when(feedbackRepository).save(new Feedback());

        feedbackService.saveFeedback(feedback, manWhoAddFeedback, manWhoGetFeedback);

        Assertions.assertEquals(expectedSetForManWhoAddFeedback, manWhoAddFeedback.getManToFeedback());
        Assertions.assertEquals(expectedSetForManWhoAddFeedback, manWhoGetFeedback.getManFromFeedback());
        Assertions.assertEquals(4, manWhoGetFeedback.getUserCountStars());

        Mockito.verify(manService).findOne(Mockito.anyInt());
        Mockito.verify(feedbackRepository).getFeedbackByManToFeedback(manWhoGetFeedback);

    }

    @Test
    void findAllFeedbacks() {
        List<Feedback> feedbacks = new ArrayList<>();
        Feedback feedback1 = new Feedback();
        Feedback feedback2 = new Feedback();
        feedbacks.add(feedback1);
        feedbacks.add(feedback2);

        Mockito.doReturn(feedbacks).when(feedbackRepository).findAll();

        List<Feedback> expectedList = feedbackService.findAllFeedbacks();

        Assertions.assertEquals(expectedList, feedbacks);
        Mockito.verify(feedbackRepository).findAll();
    }
}