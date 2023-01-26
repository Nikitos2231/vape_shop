package com.example.vape_shop.util;

import com.example.vape_shop.models.*;
import com.example.vape_shop.services.CommentService;
import com.example.vape_shop.services.FeedbackService;
import com.example.vape_shop.services.ManService;
import com.example.vape_shop.services.PurchaseRequestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PutModelerTest {

    @MockBean
    private CommentService commentService;
    @MockBean
    private PurchaseRequestService purchaseRequestService;
    @MockBean
    private FeedbackService feedbackService;
    @MockBean
    private ManService manService;
    @Autowired
    private PutModeler putModeler;

    @Test
    void putInModelAttrCommentsForItemTest() {
        Item item = new Item();
        Comment comment = new Comment();
        List<Comment> comments = Collections.singletonList(comment);
        item.setComments(comments);
        Mockito.doReturn(comments).when(commentService)
                .removeTheSameElementsIntoCommentList(Mockito.anyList());
        Model model = new ConcurrentModel();
        putModeler.putInModelAttrCommentsForItem(model, item);
        Assertions.assertNotNull(model.getAttribute("comments"));
        Assertions.assertEquals(item.getComments(), model.getAttribute("comments"));
        Mockito.verify(commentService, Mockito.times(1))
                .removeTheSameElementsIntoCommentList(Mockito.anyList());
    }

    @Test
    void putInModelAttrForAnonymousUserTest() {
        Model model = new ConcurrentModel();
        putModeler.putInModelAttrForAnonymousUser(model);
        Assertions.assertTrue(model.containsAttribute("isOwn"));
        Assertions.assertTrue(model.containsAttribute("isAnonymous"));
        Assertions.assertEquals(model.getAttribute("isOwn"), false);
        Assertions.assertEquals(model.getAttribute("isAnonymous"), true);
    }

    @Test
    void putInModelAttrIsPageBelongToEnteredMan_OwnManTest() {
        Man ownMan = new Man();
        ownMan.setUserId(1);
        Man enteredMan = new Man();
        enteredMan.setUserId(1);
        Model model = new ConcurrentModel();
        putModeler.putInModelAttrIsPageBelongToEnteredMan(model, enteredMan, ownMan);

        Assertions.assertNotNull(model.getAttribute("isOwn"));
        Assertions.assertEquals(model.getAttribute("isOwn"), true);
    }

    @Test
    void putInModelAttrIsPageBelongToEnteredMan_NotOwnManTest() {
        Man ownMan = new Man();
        ownMan.setUserId(1);
        Man enteredMan = new Man();
        enteredMan.setUserId(2);
        Model model = new ConcurrentModel();
        putModeler.putInModelAttrIsPageBelongToEnteredMan(model, enteredMan, ownMan);

        Assertions.assertNotNull(model.getAttribute("isOwn"));
        Assertions.assertEquals(model.getAttribute("isOwn"), false);
    }

    @Test
    void putInModelAttrIsAdmin_AdminTest() {
        Model model = new ConcurrentModel();
        Man enteredMan = new Man();
        enteredMan.setUserRole("ROLE_ADMIN");
        putModeler.putInModelAttrIsAdmin(model, enteredMan);
        Assertions.assertNotNull(model.getAttribute("isAdmin"));
        Assertions.assertEquals(model.getAttribute("isAdmin"), true);
    }

    @Test
    void putInModelAttrIsAdmin_NotAdminTest() {
        Model model = new ConcurrentModel();
        Man enteredMan = new Man();
        enteredMan.setUserRole("ROLE_USER");
        putModeler.putInModelAttrIsAdmin(model, enteredMan);
        Assertions.assertNotNull(model.getAttribute("isAdmin"));
        Assertions.assertEquals(model.getAttribute("isAdmin"), false);
    }

    @Test
    void putInModelAttrIsBookedItem_NotBookedTest() {
        Item item = new Item();
        item.setItemId(1);
        Man enteredMan = new Man();
        Model model = new ConcurrentModel();
        PurchaseRequest purchaseRequest1 = new PurchaseRequest();
        purchaseRequest1.setItem(new Item(2));
        PurchaseRequest purchaseRequest2 = new PurchaseRequest();
        purchaseRequest2.setItem(new Item(3));
        Set<PurchaseRequest> purchaseRequestSet = new HashSet<>(Set.of(purchaseRequest1, purchaseRequest2));

        Mockito.doReturn(purchaseRequestSet.stream().toList()).when(purchaseRequestService).findByMan(enteredMan);

        putModeler.putInModelAttrIsBookedItem(model, enteredMan, item);
        Assertions.assertNotNull(model.getAttribute("isBookedItem"));
        Assertions.assertEquals(model.getAttribute("isBookedItem"), false);

        Mockito.verify(purchaseRequestService).findByMan(enteredMan);
    }

    @Test
    void putInModelAttrIsBookedItem_BookedTest() {
        Item item = new Item();
        item.setItemId(1);
        Man enteredMan = new Man();
        Model model = new ConcurrentModel();
        PurchaseRequest purchaseRequest1 = new PurchaseRequest();
        purchaseRequest1.setItem(new Item(3));
        PurchaseRequest purchaseRequest2 = new PurchaseRequest();
        purchaseRequest2.setItem(new Item(1));
        Set<PurchaseRequest> purchaseRequestSet = new HashSet<>(Set.of(purchaseRequest1, purchaseRequest2));

        Mockito.doReturn(purchaseRequestSet.stream().toList()).when(purchaseRequestService).findByMan(enteredMan);

        putModeler.putInModelAttrIsBookedItem(model, enteredMan, item);
        Assertions.assertNotNull(model.getAttribute("isBookedItem"));
        Assertions.assertEquals(model.getAttribute("isBookedItem"), true);

        Mockito.verify(purchaseRequestService).findByMan(enteredMan);
    }

    @Test
    void putInModelAttrFeedbackForMan_OneFeedbackTest() {
        Model model = new ConcurrentModel();
        Man manWhoGotFeedbacks = new Man();
        List<Feedback> feedbacks = new ArrayList<>();
        feedbacks.add(new Feedback());
        Mockito.doReturn(feedbacks).when(feedbackService).getFeedbacksForMan(manWhoGotFeedbacks);

        putModeler.putInModelAttrFeedbackForMan(model, manWhoGotFeedbacks);

        Assertions.assertNotNull(model.getAttribute("feedbackForMan"));
        Assertions.assertEquals(model.getAttribute("feedbackForMan"), feedbacks);
        Mockito.verify(feedbackService).getFeedbacksForMan(manWhoGotFeedbacks);
    }

    @Test
    void putInModelAttrFeedbackForMan_NoFeedbackTest() {
        Model model = new ConcurrentModel();
        Man manWhoGotFeedbacks = new Man();
        List<Feedback> feedbacks = new ArrayList<>();
        Mockito.doReturn(feedbacks).when(feedbackService).getFeedbacksForMan(manWhoGotFeedbacks);

        putModeler.putInModelAttrFeedbackForMan(model, manWhoGotFeedbacks);

        Assertions.assertNull(model.getAttribute("feedbackForMan"));
        Mockito.verify(feedbackService).getFeedbacksForMan(manWhoGotFeedbacks);
    }

    @Test
    void putInModelAttrWasManEstimated_AlreadyEstimatedTest() {
        Model model = new ConcurrentModel();
        Man ownMan = new Man();
        ownMan.setUserId(2);
        Man enteredMan = new Man();
        enteredMan.setUserId(1);
        List<Feedback> feedbacks = new ArrayList<>();
        Feedback feedback = new Feedback();
        feedback.setManFromFeedback(enteredMan);
        feedback.setManToFeedback(ownMan);
        feedbacks.add(feedback);

        Mockito.doReturn(feedbacks).when(feedbackService).findAllFeedbacks();
        Mockito.doReturn(enteredMan).when(manService).getEnteredMan();

        putModeler.putInModelAttrWasManEstimated(model, ownMan);

        Assertions.assertNotNull(model.getAttribute("wasManEstimated"));
        Assertions.assertEquals(model.getAttribute("wasManEstimated"), true);
        Mockito.verify(feedbackService).findAllFeedbacks();
        Mockito.verify(manService).getEnteredMan();
    }

    @Test
    void putInModelAttrWasManEstimated_NotEstimatedTest() {
        Model model = new ConcurrentModel();
        Man ownMan = new Man();
        ownMan.setUserId(1);
        Man enteredMan = new Man();
        enteredMan.setUserId(10);
        List<Feedback> feedbacks = new ArrayList<>();
        Feedback feedback = new Feedback();
        feedback.setManFromFeedback(new Man());
        feedback.setManToFeedback(ownMan);
        feedbacks.add(feedback);

        Mockito.doReturn(feedbacks).when(feedbackService).findAllFeedbacks();
        Mockito.doReturn(enteredMan).when(manService).getEnteredMan();

        putModeler.putInModelAttrWasManEstimated(model, ownMan);

        Assertions.assertNotNull(model.getAttribute("wasManEstimated"));
        Assertions.assertEquals(model.getAttribute("wasManEstimated"), false);
        Mockito.verify(feedbackService).findAllFeedbacks();
        Mockito.verify(manService).getEnteredMan();
    }

    @Test
    void putInModelAttrEnteredManForMainPage_AnonymousUserTest() {
        Model model = new ConcurrentModel();
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.doReturn("anonymousUser").when(authentication).getPrincipal();

        putModeler.putInModelAttrEnteredManForMainPage(model, authentication);

        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
        Assertions.assertNull(model.getAttribute("man"));
    }

    @Test
    void putInModelAttrEnteredManForMainPage_NotAnonymousUserTest() {
        Model model = new ConcurrentModel();
        Authentication authentication = Mockito.mock(Authentication.class);
        Man enteredMan = new Man();
        enteredMan.setUserId(1);

        Mockito.doReturn("some name").when(authentication).getPrincipal();
        Mockito.doReturn(enteredMan).when(manService).getEnteredMan();

        putModeler.putInModelAttrEnteredManForMainPage(model, authentication);

        Assertions.assertNotNull(model.getAttribute("man"));
        Assertions.assertEquals(model.getAttribute("man"), enteredMan);
        Mockito.verify(authentication, Mockito.times(1)).getPrincipal();
    }
}