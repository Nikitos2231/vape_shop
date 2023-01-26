package com.example.vape_shop.item_util;

import com.example.vape_shop.models.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@ExtendWith({MockitoExtension.class})
class ItemSortingTest {

    @InjectMocks
    private ItemSorting itemSorting;

    @Test
    void sortItemsByDate() throws InterruptedException {
        List<Item> itemList = new ArrayList<>();
        Item item = new Item(new Date());
        Thread.sleep(20);
        Item item1 = new Item(new Date());
        Thread.sleep(20);
        Item item2 = new Item(new Date());
        itemList.add(item2);
        itemList.add(item);
        itemList.add(item1);
        List<Item> actual = itemSorting.sortItemsByDate(itemList);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(List.of(item2, item1, item), actual);
    }

    @Test
    void getCorrectSortedItemList() {
        List<Item> itemList = new ArrayList<>();
        Item item = new Item(1, true, "YES");
        Item item1 = new Item(1, false, "YES");
        Item item2 = new Item(2, false, "NO");
        Item item3 = new Item(3, false, "YES");

        itemList.add(item2);
        itemList.add(item);
        itemList.add(item1);
        itemList.add(item3);

        List<Item> actual = itemSorting.getCorrectSortedItemList(itemList);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(List.of(item1, item3), actual);

    }

    @Test
    void removeTheSameElementsIntoItemList() {
        List<Item> itemList = new ArrayList<>();
        Item item = new Item(1);
        Item item1 = new Item(1);
        Item item2 = new Item(2);
        itemList.add(item2);
        itemList.add(item);
        itemList.add(item1);

        List<Item> actual = itemSorting.removeTheSameElementsIntoItemList(itemList);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(List.of(item2, item1), actual);
    }

    @Test
    void getFilteredAndUniqAndSortByDateItemsWhereAttrIsSoldEqualYes() throws InterruptedException {
        Item item = new Item(1, true, new Date());
        Thread.sleep(20);
        Item item1 = new Item(1, true, new Date());
        Thread.sleep(20);
        Item item2 = new Item(2, false, new Date());
        Thread.sleep(20);
        Item item3 = new Item(3, true, new Date());

        List<Item> actual = itemSorting
                .getFilteredAndUniqAndSortByDateItemsWhereAttrIsSoldEqualYes(List.of(item, item1, item2, item3));
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(List.of(item3, item1), actual);
    }

    @Test
    void sortByDateBoughtItemsByMan() throws InterruptedException {
        Item item = new Item(1, new Date());
        Thread.sleep(20);
        Item item1 = new Item(2, new Date());
        Thread.sleep(20);
        Item item2 = new Item(1, new Date());
        Thread.sleep(20);
        Item item3 = new Item(3, new Date());

        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item1);
        items.add(item2);
        items.add(item3);

        List<Item> actual = itemSorting.sortByDateBoughtItemsByMan(items);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(List.of(item3, item1, item2), actual);
    }

}