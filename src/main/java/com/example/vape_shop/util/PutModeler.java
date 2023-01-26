package com.example.vape_shop.util;

import com.example.vape_shop.models.*;
import com.example.vape_shop.services.CommentService;
import com.example.vape_shop.services.FeedbackService;
import com.example.vape_shop.services.ManService;
import com.example.vape_shop.services.PurchaseRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class PutModeler {

    private final CommentService commentService;
    private final PurchaseRequestService purchaseRequestService;
    private final FeedbackService feedbackService;
    private final ManService manService;

    @Autowired
    public PutModeler(CommentService commentService, PurchaseRequestService purchaseRequestService, FeedbackService feedbackService, ManService manService) {
        this.commentService = commentService;
        this.purchaseRequestService = purchaseRequestService;
        this.feedbackService = feedbackService;
        this.manService = manService;
    }

    public void putInModelAttrCommentsForItem(Model model, Item item) {
        List<Comment> commentsForItem = item.getComments();
        commentService.removeTheSameElementsIntoCommentList(commentsForItem);
        model.addAttribute("comments", commentsForItem);
    }

    public void putInModelAttrForAnonymousUser(Model model) {
        model.addAttribute("isOwn", false);
        model.addAttribute("isAnonymous", true);
    }

    public void putInModelAttrIsPageBelongToEnteredMan(Model model, Man enteredMan, Man ownMan) {
        if (ownMan.getUserId() != enteredMan.getUserId()) {
            model.addAttribute("isOwn", false);
        } else {
            model.addAttribute("isOwn", true);
        }
    }

    public void putInModelAttrIsAdmin(Model model, Man enteredMan) {
        if (enteredMan.getUserRole().equals("ROLE_ADMIN")) {
            model.addAttribute("isAdmin", true);
        }
        else {
            model.addAttribute("isAdmin", false);
        }
    }

    public void putInModelAttrIsBookedItem(Model model, Man enteredMan, Item item) {
        model.addAttribute("isBookedItem", false);
        List<PurchaseRequest> purchaseRequestSet = purchaseRequestService.findByMan(enteredMan);
        if (purchaseRequestSet != null) {
            for (PurchaseRequest purchaseRequest : purchaseRequestSet) {
                if (purchaseRequest.getItem().getItemId() == item.getItemId()) {
                    model.addAttribute("isBookedItem", true);
                    break;
                }
            }
        }
    }

    public void putInModelAttrFeedbackForMan(Model model, Man manWhoGotFeedbacks) {
        List<Feedback> feedbacksForMan = feedbackService.getFeedbacksForMan(manWhoGotFeedbacks);
        if (feedbacksForMan.size() == 0) {
            model.addAttribute("feedbackForMan", null);
            return;
        }
        model.addAttribute("feedbackForMan", feedbacksForMan);
    }

    public void putInModelAttrWasManEstimated(Model model, Man ownMan) {
        Man enteredMan = manService.getEnteredMan();
        List<Feedback> feedbackSet = feedbackService.findAllFeedbacks();
        for (Feedback feedback : feedbackSet) {
            if ((feedback.getManFromFeedback().getUserId() == enteredMan.getUserId()) && (feedback.getManToFeedback().getUserId() == ownMan.getUserId())) {
                model.addAttribute("wasManEstimated", true);
                return;
            }
        }
        model.addAttribute("wasManEstimated", false);
    }

    public void putInModelAttrEnteredManForMainPage(Model model, Authentication authentication) {
        if (!authentication.getPrincipal().equals("anonymousUser")) {
            Man man = manService.getEnteredMan();
            model.addAttribute("man", man);
        }
        else {
            model.addAttribute("man", null);
        }
    }


}
