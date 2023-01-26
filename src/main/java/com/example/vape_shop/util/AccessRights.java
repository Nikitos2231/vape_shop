package com.example.vape_shop.util;

import com.example.vape_shop.item_util.ItemSorting;
import com.example.vape_shop.models.*;
import com.example.vape_shop.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccessRights {

    private final ItemService itemService;
    private final ItemSorting itemSorting;
    private final PutModeler putModeler;
    private  final ManService manService;

    @Autowired
    public AccessRights(ItemService itemService, ItemSorting itemSorting, PutModeler putModeler, ManService manService) {
        this.itemService = itemService;
        this.itemSorting = itemSorting;
        this.putModeler = putModeler;
        this.manService = manService;
    }

    public void fillModelForItemPage(Authentication authentication, Model model, Man ownMan, Item item) {
        model.addAttribute("isAnonymous", false);
        model.addAttribute("item", item);
        model.addAttribute("man", ownMan);
        putModeler.putInModelAttrCommentsForItem(model, item);
        if (manService.isManAuth(authentication)) {
            Man enteredMan = manService.getEnteredMan();
            model.addAttribute("enteredMan", enteredMan);
            model.addAttribute("canEstimate", canEstimateMan(ownMan, enteredMan));
            model.addAttribute("isItemSold", item.isSold());
            putModeler.putInModelAttrIsAdmin(model, enteredMan);
            putModeler.putInModelAttrIsPageBelongToEnteredMan(model, enteredMan, ownMan);
            putModeler.putInModelAttrIsBookedItem(model, enteredMan, item);
        }
        else {
            putModeler.putInModelAttrForAnonymousUser(model);
        }
    }

//    public boolean isManAuth(Authentication authentication) {
//        return !authentication.getPrincipal().equals("anonymousUser");
//    }

    public void fillModelForProfilePage(Authentication authentication, Model model, Man ownMan) {
        model.addAttribute("isAnonymous", false);
        if (manService.isManAuth(authentication)) {
            Man enteredMan = manService.getEnteredMan();
            model.addAttribute("enteredMan", enteredMan);
            model.addAttribute("canEstimate", canEstimateMan(ownMan, enteredMan));
            model.addAttribute("items", ownMan.getItems().stream().filter(item -> !item.isSold()).collect(Collectors.toList()));
            model.addAttribute("boughtItems", itemSorting.sortByDateBoughtItemsByMan(ownMan.getBoughtItems()));
            model.addAttribute("soldItems", itemService.getItemsForManWhereItemIsSoldEqualsYes(ownMan));
            putModeler.putInModelAttrFeedbackForMan(model, ownMan);
            putModeler.putInModelAttrWasManEstimated(model, ownMan);
            putModeler.putInModelAttrIsAdmin(model, enteredMan);
            putModeler.putInModelAttrIsPageBelongToEnteredMan(model, enteredMan, ownMan);
        }
        else {
            putModeler.putInModelAttrForAnonymousUser(model);
        }
    }

    private boolean canEstimateMan(Man ownMan, Man enteredMan) {
        List<Item> listItemsWhichWereSold = ownMan.getItems().stream().filter(Item::isSold).toList();
        for (Item item : listItemsWhichWereSold) {
            if (item.getBuyer().getUserId() == enteredMan.getUserId()) {
                return true;
            }
        }
        return false;
    }
}
