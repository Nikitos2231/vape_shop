package com.example.vape_shop.controllers;

import com.example.vape_shop.exceptions.IncorrectAmountImagesToUploadException;
import com.example.vape_shop.models.Feedback;
import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.services.FeedbackService;
import com.example.vape_shop.services.ItemService;
import com.example.vape_shop.services.ManService;
import com.example.vape_shop.util.AccessRights;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ManControllers {

    private static final Logger logger = LogManager.getLogger(ManControllers.class);
    private final ManService manService;
    private final ItemService itemService;
    private final FeedbackService feedbackService;
    private final AccessRights accessRights;
    private String searchManByIdOrEmail = "";

    @Autowired
    public ManControllers(ManService manService, ItemService itemService, FeedbackService feedbackService, AccessRights accessRights) {
        this.manService = manService;
        this.itemService = itemService;
        this.feedbackService = feedbackService;
        this.accessRights = accessRights;
    }

    @GetMapping("/man/{man_id}")
    public String getProfilePage(Model model, @PathVariable("man_id") int manId) {
        Man ownMan = manService.findOne(manId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        accessRights.fillModelForProfilePage(authentication, model, ownMan);
        model.addAttribute("man", ownMan);
        model.addAttribute("items", itemService.getCheckedItemsForPeopleSortedByDate(ownMan));
        model.addAttribute("itemsNoChecked", itemService.getNoCheckedItemsForPeopleSortedByDate(ownMan));
        return "profile";
    }

    @PatchMapping("/man/{man_id}/update")
    public String editMan(@PathVariable("man_id") int oldManId, @ModelAttribute("man") @Valid Man updatedMan, BindingResult result, Model model) {
        manService.inheritAttributesForUpdatedManFromOldMan(updatedMan, oldManId);
        Man verifiedMan = manService.findOne(oldManId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        accessRights.fillModelForProfilePage(authentication, model, verifiedMan);
        if (result.hasFieldErrors("userName") || result.hasFieldErrors("userDateOfBirth") || result.hasFieldErrors("userSurname")) {
            logger.warn("Человек с id: {} ввел невалидные данные при изменении своей страницы", oldManId);
            return "profile";
        }
        manService.saveMan(updatedMan);
        logger.info("Человек с id: {} изменил данные своей страницы", oldManId);
        return "redirect:/man/{man_id}";
    }

    @GetMapping("/man/{man_id}/add_avatar")
    public String getAddAvatarPage(Model model, @PathVariable("man_id") int id) {
        model.addAttribute("man", manService.findOne(id));
        return "add_avatar";
    }

    @PatchMapping("/man/{man_id}/add_avatar")
    public String addAvatar(Model model, @RequestParam("avatar") MultipartFile imageForMan, @PathVariable("man_id") int manId) throws IOException {
        Man man = manService.findOne(manId);
        model.addAttribute("man", man);
        model.addAttribute("isOwn", true);
        model.addAttribute("items", man.getItems().stream().filter(item -> !item.isSold()).collect(Collectors.toList()));
        if (!manService.isManUpdated(man, imageForMan)) {
            model.addAttribute("message", "Фотография не загружена, убедитесь что вы выбрали одну фотографию и ее размер не превышает 1мб");
            logger.warn("Человек с id: {} не смог обновить фотографию в профиле", manId);
            return "add_avatar";
        }
        logger.info("Человек с id: {} обновил фотографию в профиле", manId);
        return "redirect:/man/{man_id}";
    }

    @GetMapping("/man/{man_id}/add_card")
    public String getAddCardPage(@PathVariable("man_id") int id, Model model, @ModelAttribute("item") Item item) {
        model.addAttribute("man", manService.findOne(id));
        return "add_card";
    }

    @PostMapping("/man/{man_id}/add_card")
    public String addCard(@ModelAttribute("item") @Valid Item item, BindingResult bindingResult,
                          @RequestParam("avatar") List<MultipartFile> multipartFiles,
                          @PathVariable("man_id") int manId, Model model) {
        Man man = manService.findOne(manId);
        model.addAttribute("man", man);
        item.setMan(man);
        if (bindingResult.hasErrors()) {
            logger.warn("Человек с id: {} ввел неккоректные данны при создании товара", manId);
            return "add_card";
        }
        try {
            if (!itemService.isItemSaved(item, multipartFiles)) {
                model.addAttribute("error_message", "Пожалуйста, удостоверьтесь, что вы выбрали от 1 до 3 фотографий и каждая не превышает размер в 1Мб");
                logger.warn("Человек с id: {} поставил невалидные фотографии при создании товара", manId);
                return "add_card";
            }
        } catch (IncorrectAmountImagesToUploadException | FileSizeLimitExceededException | DirectoryNotEmptyException e) {
            e.printStackTrace();
            return "add_card";
        }
        logger.info("Человек с id: {} успешно создал товар с id: {}", manId, item.getItemId());
        return "redirect:/man/{man_id}";
    }

    @GetMapping("/admin/{man_id}")
    public String getAdminPage(@PathVariable("man_id") int id, Model model) {
        model.addAttribute("items", itemService.findAllByItemChecked("NO"));
        model.addAttribute("man", manService.findOne(id));
        model.addAttribute("foundPeople", manService.getFoundPeople(searchManByIdOrEmail));
        model.addAttribute("searchString", searchManByIdOrEmail);
        return "admin";
    }

    @PostMapping("/admin/{man_id}/search_man")
    public String searchManByIdOrEmail(@PathVariable("man_id") int manId,
                                       @RequestParam("search_man") String searchMan) {
        searchManByIdOrEmail = searchMan;
        return "redirect:/admin/{man_id}";
    }

    @PostMapping("/items/{item_id}/activate_card")
    public String activateItemCard(@PathVariable("item_id") int itemId) {
        Man admin = manService.getEnteredMan();
        itemService.activatedCard(itemService.findById(itemId));
        logger.info("Админ с id: {} одобрил товар с id: {}", admin.getUserId(), itemId);
        return "redirect:/admin/" + admin.getUserId();
    }

    @PostMapping("/items/{item_id}/reject_card")
    public String rejectItemCard(@PathVariable("item_id") int itemId) throws IOException {
        Man admin = manService.getEnteredMan();
        itemService.deleteItem(itemService.findById(itemId));
        logger.info("Админ с id: {} отклонил товар с id: {}", admin.getUserId(), itemId);
        return "redirect:/admin/" + admin.getUserId();
    }

    @GetMapping("/man/{man_id}/estimate")
    public String getEstimateManPage(@PathVariable("man_id") int manId, Model model, @ModelAttribute("feedback") Feedback feedback) {
        model.addAttribute("enteredMan", manService.getEnteredMan());
        model.addAttribute("man", manService.findOne(manId));
        return "estimate";
    }

    @PostMapping("/man/{man_id}/estimate")
    public String addFeedback(@ModelAttribute("feedback") @Valid Feedback feedback, @PathVariable("man_id") int manId, @RequestParam("rating") int ratingForFeddback) {
        feedback.setFeedbackRating(ratingForFeddback);
        Man enteredMan = manService.getEnteredMan();
        Man ownMan = manService.findOne(manId);
        feedbackService.saveFeedback(feedback, enteredMan, ownMan);
        logger.info("Человек с id: {} оставил комментарий человеку с id: {}", enteredMan.getUserId(), ownMan.getUserId());
        return "redirect:/man/{man_id}";
    }




}
