package com.example.vape_shop.controllers;

import com.example.vape_shop.models.Item;
import com.example.vape_shop.services.ItemService;
import com.example.vape_shop.util.AccessRights;
import com.example.vape_shop.util.PutModeler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);
    private final ItemService itemService;
    private final AccessRights accessRights;
    private final PutModeler putModeler;
    private String searchText = "";
    private String typeOfSort = "date";

    @Autowired
    public MainController(ItemService itemService, AccessRights accessRights, PutModeler putModeler) {
        this.itemService = itemService;
        this.accessRights = accessRights;
        this.putModeler = putModeler;
    }

    @GetMapping("/main")
    public String getMainPage(Model model) {
        List<Item> items =  itemService.findItemsByNameOrderBySomething(searchText, typeOfSort);
        model.addAttribute("items", items);
        model.addAttribute("search", searchText);
        model.addAttribute("count", items.size());
        model.addAttribute("sort", typeOfSort);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        putModeler.putInModelAttrEnteredManForMainPage(model, authentication);
        return "main";
    }

    @PostMapping("/main/search")
    public String searchItems(@RequestParam("search_text") String search_text) {
        if (!search_text.isEmpty()) {
            logger.info("Человек ввел в поиск: {}", search_text);
        }
        searchText = search_text;
        return "redirect:/main";
    }

    @PostMapping("/main/sort")
    public String sortItems(@RequestParam("sort_item") String sort_item) {
        logger.info("Человек выбрал сортировку: {}", sort_item);
        typeOfSort = sort_item;
        return "redirect:/main";
    }

    @PostMapping("/main/test")
    public String postTest() {
        return "redirect:/main";
    }

}
