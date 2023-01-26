package com.example.vape_shop.item_util;

import com.example.vape_shop.models.Item;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ItemSorting {

    public List<Item> sortItemsByDate(List<Item> items) {
        Collections.sort(items);
        Collections.reverse(items);
        return items;
    }

    public List<Item> getCorrectSortedItemList(List<Item> items) {
        return getItemsWithoutItemsWhereItemCheckedEqualNo(getItemsWithoutSold(items));
    }

    private List<Item> getItemsWithoutItemsWhereItemCheckedEqualNo(List<Item> items) {
        return items.stream().filter(item -> item.getItemChecked().equals("YES")).collect(Collectors.toList());
    }

    private List<Item> getItemsWithoutSold(List<Item> items) {
        return items.stream().filter(item -> !item.isSold()).collect(Collectors.toList());
    }

    public List<Item> removeTheSameElementsIntoItemList(List<Item> items) {
        Set<Item> set = new HashSet<>(items);
        items.clear();
        items.addAll(set);
        Collections.reverse(items);
        return items;
    }

    private List<Item> filterItemsByIsSoldAttr(List<Item> items) {
        return items.stream().filter(Item::isSold).collect(Collectors.toList());
    }

    public List<Item> getFilteredAndUniqAndSortByDateItemsWhereAttrIsSoldEqualYes(List<Item> items) {
        return sortSoldItemsByDate(removeTheSameElementsIntoItemList(filterItemsByIsSoldAttr(items)));
    }

    public List<Item> sortByDateBoughtItemsByMan(List<Item> items) {
        return sortSoldItemsByDate(removeTheSameElementsIntoItemList(items));
    }

    private List<Item> sortSoldItemsByDate(List<Item> items) {
        Collections.sort(items);
        Collections.reverse(items);
        return items;
    }
}
