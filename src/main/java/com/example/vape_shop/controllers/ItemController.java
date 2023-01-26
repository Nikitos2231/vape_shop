package com.example.vape_shop.controllers;

import com.example.vape_shop.models.Comment;
import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.models.PurchaseRequest;
import com.example.vape_shop.services.CommentService;
import com.example.vape_shop.services.ItemService;
import com.example.vape_shop.services.ManService;
import com.example.vape_shop.services.PurchaseRequestService;
import com.example.vape_shop.util.AccessRights;
import com.example.vape_shop.util.ImageWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Date;
import java.util.List;

@Controller
public class ItemController {

    private static final Logger logger = LogManager.getLogger(ItemController.class);
    private final ItemService itemService;
    private final ManService manService;
    private final AccessRights accessRights;
    private final CommentService commentService;
    private final PurchaseRequestService purchaseRequestService;
    private final ImageWorker imageWorker;


    @Autowired
    public ItemController(ItemService itemService, ManService manService, AccessRights accessRights, CommentService commentService, PurchaseRequestService purchaseRequestService, ImageWorker imageWorker) {
        this.itemService = itemService;
        this.manService = manService;
        this.accessRights = accessRights;
        this.commentService = commentService;
        this.purchaseRequestService = purchaseRequestService;
        this.imageWorker = imageWorker;
    }

    @GetMapping("/items/{item_id}")
    public String getItemPage(@PathVariable("item_id") int itemId, Model model, @ModelAttribute("comment") Comment comment) {
        Item item =  itemService.findById(itemId);
        Man ownMan = manService.findManByItem(item);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        accessRights.fillModelForItemPage(authentication, model, ownMan, item);
        return "card";
    }

    @PostMapping("/items/{item_id}/create_message")
    public String createMessage(@PathVariable("item_id") int itemId, @ModelAttribute("comment") @Valid Comment comment,
                                BindingResult bindingResult, Model model) {
        Item item = itemService.findById(itemId);
        Man enteredMan = manService.getEnteredMan();
        Man ownMan = manService.findManByItem(item);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        accessRights.fillModelForItemPage(authentication, model, ownMan, item);
        if (bindingResult.hasErrors()) {
            model.addAttribute("message_error", "В сообщении должно быть от 5 до 100 символов!");
            logger.warn("Человек с id: {} ввел невалидный комментарий к посту с id: {}", enteredMan.getUserId(), itemId);
            return "card";
        }
        commentService.save(comment, enteredMan, item);
        logger.info("Человек с id: {} добавил комментарий к посту с id: {}", enteredMan.getUserId(), item.getItemId());
        return "redirect:/items/{item_id}";
    }

    @GetMapping("/items/{item_id}/update_item")
    public String getUpdateItemPage(@PathVariable("item_id") int id, @ModelAttribute("item") Item item, Model model) {
        model.addAttribute("item", itemService.findById(id));
        return "update_card";
    }

    @DeleteMapping("/items/{item_id}/delete_item")
    public String deleteItem(@PathVariable("item_id") int id) throws IOException {
        logger.info("Человек с id: {} удалил свой пост", manService.getEnteredMan().getUserId());
        itemService.deleteItem(itemService.findById(id));
        return "redirect:/main";
    }

    @PatchMapping("/items/{item_id}/update_item")
    public String updateItem(@PathVariable("item_id") int itemId,
                             @ModelAttribute("item") @Valid Item item,
                             BindingResult bindingResult,
                             @RequestParam("imagesForItem") List<MultipartFile> imagesForItem,
                             Model model) throws IOException {
        List<String> oldImages = itemService.getListAvatarsFromItem(itemService.findById(itemId));
        Man enteredMan = manService.getEnteredMan();
        if (bindingResult.hasErrors()) {
            logger.warn("Человек с id: {} не смог изменить пост с id: {}", enteredMan.getUserId(), itemId);
            return "update_card";
        }
        if (!itemService.isItemUpdate(itemId, item, imagesForItem)) {
            model.addAttribute("error_message", "Пожалуйста, удостоверьтесь, что вы выбрали от 1 до 3 фотографий и каждая не превышает размер в 1Мб");
            logger.warn("Человек с id: {} неправильно загрузил фотографии при редактировании поста с id: {}", enteredMan.getUserId(), itemId);
            return "update_card";
        }
        logger.info("Человек с id: {} успешно редактировал свой пост с id: {}", enteredMan.getUserId(), itemId);
        imageWorker.clearOldImages(oldImages);
        return "redirect:/items/{item_id}";
    }

    @GetMapping("/items/{item_id}/submit_sale_item")
    public String getSubmitSaleItemPage(@PathVariable("item_id") int id, Model model) {
        Item item = itemService.findById(id);
        model.addAttribute("item", item);
        model.addAttribute("peopleWhichToBookItem", purchaseRequestService.getPeopleWhichToBookThisItem(item));
        model.addAttribute("man", manService.findManByItem(item));
        return "submit_sale_item";
    }

    @PostMapping("/items/{item_id}/submit_sale_item")
    public String toBookItem(@PathVariable("item_id") int itemId) {
        Item item = itemService.findById(itemId);
        Man man = manService.getEnteredMan();
        purchaseRequestService.save(item, man);
        logger.info("Человек с id: {} забронировал товар с id: {}", man.getUserId(), itemId);
        return "redirect:/items/{item_id}";
    }

    @PostMapping("/items/{item_id}/submit_sale_item/submit")
    public String submitSale(@PathVariable("item_id") int itemId, @RequestParam("selected_email") String email) {
        Item item = itemService.findById(itemId);
        List<PurchaseRequest> purchaseRequests = purchaseRequestService.findByItem(item);
        Man enteredMan = manService.getEnteredMan();
        item.setBuyer(manService.getManByEmail(email));
        item.setItemDateOfSale(new Date());
        itemService.save(item);
        logger.info("Человек с id: {} подтвердил продажу товара с id: {}. Email покупателя - {}", enteredMan.getUserId(), itemId, email);
        purchaseRequestService.deleteAllConnectionsFromPurchaseList(purchaseRequests);
        return "redirect:/main";
    }
}
