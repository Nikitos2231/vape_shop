package com.example.vape_shop.services;

import com.example.vape_shop.exceptions.IncorrectAmountImagesToUploadException;
import com.example.vape_shop.item_util.ItemChecker;
import com.example.vape_shop.item_util.ItemCreator;
import com.example.vape_shop.item_util.ItemSorting;
import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.repositories.ItemRepository;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class) // Запускается Spring context
class ItemServiceTest {

//    @InjectMocks // Инициализация настоящим классом, его поля будут аннотированны Mock'ами
    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ItemSorting itemSorting;

    @MockBean
    private ItemChecker itemChecker;

    @Test
    void activatedCard() {
        Item item = new Item();
        itemService.activatedCard(item);
        Assertions.assertNotNull(item.getItemChecked());
        Assertions.assertEquals(item.getItemChecked(), "YES");
    }

    @Test
    void findAllByMan() {
        Man man = new Man();
        Mockito.when(itemRepository.findAllByMan(man)).thenReturn(Mockito.anyList());
        List<Item> items = itemService.findAllByMan(man);
        Assertions.assertNotNull(items);
        Mockito.verify(itemRepository).findAllByMan(man);
    }

    @Test
    void findByIdSuccessTest() {
        Item item = new Item();
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item)).thenReturn(null);

        Mockito.doReturn(Optional.of(new Item())).when(itemRepository).findById(312);
        Assertions.assertNotNull(itemService.findById(312));
        Mockito.verify(itemRepository, Mockito.times(1)).findById(Mockito.anyInt());
    }

    @Test
    void findByIdFailedTest() {
        Item item = new Item();
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item)).thenReturn(null);

        Mockito.doReturn(Optional.empty()).when(itemRepository).findById(-123);
        Assertions.assertNull(itemService.findById(-123));
        Mockito.verify(itemRepository, Mockito.times(1)).findById(Mockito.anyInt());
    }

    @Test
    void findAllByManAndItemChecked() {
        Man man = new Man();
        Item item1 = new Item();
        Item item2 = new Item();
        item1.setSold(true);
        item1.setItemChecked("NO");
        item2.setSold(false);
        item1.setItemChecked("NO");
        Set<Item> itemSet = new HashSet<>();
        Set<Item> itemSetRight = new HashSet<>();
        itemSetRight.add(item2);
        itemSet.add(item1);
        itemSet.add(item2);
        man.setItems(itemSet);
        Mockito.doReturn(itemSetRight.stream().toList()).when(itemRepository).findAllByManAndItemCheckedAndIsSoldEqualFalse(Mockito.anyInt(), Mockito.anyString());
        Mockito.doReturn(itemSetRight.stream().toList()).when(itemSorting).removeTheSameElementsIntoItemList(Mockito.anyList());

        List<Item> items = itemService.findAllByManAndItemChecked(1, "NO");
        Assertions.assertArrayEquals(itemSetRight.toArray(), items.toArray());
        Assertions.assertNotNull(items);
    }

    @Test
    void findAllByItemChecked() {
        Mockito.when(itemRepository.findAllByItemChecked(Mockito.anyString())).thenReturn(Mockito.anyList());
        List<Item> items = itemService.findAllByItemChecked("NO");
        Mockito.verify(itemRepository, Mockito.times(1)).findAllByItemChecked(Mockito.anyString());
        Assertions.assertNotNull(items);
    }

    @Test
    void getListAvatarsFromItem() {
        Item item = new Item();
        item.setItemAvatar("some Avatar1");
        item.setItemAvatar2("some Avatar2");
        item.setItemAvatar3("some Avatar3");

        List<String> listAvatars = itemService.getListAvatarsFromItem(item);
        Assertions.assertNotNull(listAvatars);
        Assertions.assertArrayEquals(Arrays.asList(item.getItemAvatar(), item.getItemAvatar2(), item.getItemAvatar3())
                .toArray(), listAvatars.toArray());
    }

    @Test
    void getCheckedItemsForPeopleSortedByDateIfAllContainsItemCheckedYes() throws InterruptedException {
        Item item1 = new Item();
        Item item2 = new Item();
        Item item3 = new Item();
        item1.setItemChecked("Yes");
        item2.setItemChecked("Yes");
        item3.setItemChecked("Yes");

        item1.setItemDateOfCreate(new Date());
        Thread.sleep(50);
        item3.setItemDateOfCreate(new Date());
        Thread.sleep(50);
        item2.setItemDateOfCreate(new Date());
        List<Item> itemList = Arrays.asList(item1, item2, item3);
        Mockito.doReturn(itemList).when(itemSorting).sortItemsByDate(Mockito.anyList());
        List<Item> items = itemSorting.sortItemsByDate(itemList);
        Assertions.assertEquals(items, itemList);
    }

    @Test
    void getCheckedItemsForPeopleSortedByDate() {
        Man man = new Man();
        man.setUserId(1);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item();
        item1.setItemChecked("YES");
        Item item2 = new Item();
        item2.setItemChecked("YES");
        items.add(item1);
        items.add(item2);
        man.setItems(items);
        Mockito.doReturn(items.stream().toList()).when(itemRepository).findAllByManAndItemCheckedAndIsSoldEqualFalse(1, "YES");
        Mockito.doReturn(items.stream().toList()).when(itemSorting).removeTheSameElementsIntoItemList(items.stream().toList());
        Mockito.doReturn(items.stream().toList()).when(itemSorting).sortItemsByDate(items.stream().toList());

        List<Item> actual = itemService.getCheckedItemsForPeopleSortedByDate(man).stream().toList();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(items.stream().toList(), actual);
        Mockito.verify(itemRepository).findAllByManAndItemCheckedAndIsSoldEqualFalse(1, "YES");
        Mockito.verify(itemSorting).removeTheSameElementsIntoItemList(Mockito.anyList());
        Mockito.verify(itemSorting).sortItemsByDate(Mockito.anyList());
    }

    @Test
    void getNoCheckedItemsForPeopleSortedByDate() {
        Man man = new Man();
        man.setUserId(1);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item();
        item1.setItemChecked("NO");
        Item item2 = new Item();
        item2.setItemChecked("NO");
        items.add(item1);
        items.add(item2);
        man.setItems(items);
        Mockito.doReturn(items.stream().toList()).when(itemRepository).findAllByManAndItemCheckedAndIsSoldEqualFalse(1, "NO");
        Mockito.doReturn(items.stream().toList()).when(itemSorting).removeTheSameElementsIntoItemList(items.stream().toList());
        Mockito.doReturn(items.stream().toList()).when(itemSorting).sortItemsByDate(items.stream().toList());

        List<Item> actual = itemService.getNoCheckedItemsForPeopleSortedByDate(man).stream().toList();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(items.stream().toList(), actual);
        Mockito.verify(itemRepository).findAllByManAndItemCheckedAndIsSoldEqualFalse(1, "NO");
        Mockito.verify(itemSorting).removeTheSameElementsIntoItemList(Mockito.anyList());
        Mockito.verify(itemSorting).sortItemsByDate(Mockito.anyList());
    }

    @Test
    void getItemsForManWhereItemIsSoldEqualsYes() {
        Man man = new Man();
        man.setUserId(1);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item();
        item1.setSold(true);
        Item item2 = new Item();
        item2.setSold(true);
        items.add(item1);
        items.add(item2);
        man.setItems(items);
        Mockito.doReturn(items.stream().toList()).when(itemRepository).findAllByMan(man);
        Mockito.doReturn(items.stream().toList()).when(itemSorting).getFilteredAndUniqAndSortByDateItemsWhereAttrIsSoldEqualYes(items.stream().toList());

        List<Item> actual = itemService.getItemsForManWhereItemIsSoldEqualsYes(man).stream().toList();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(items.stream().toList(), actual);
        Mockito.verify(itemRepository).findAllByMan(man);
        Mockito.verify(itemSorting).getFilteredAndUniqAndSortByDateItemsWhereAttrIsSoldEqualYes(Mockito.anyList());
    }

    @Test
    void save() {
        Item item = new Item(1);
        itemService.save(item);
        Mockito.verify(itemRepository).save(item);
    }

    @Test
    void isItemSaved_SuccessTest() throws FileSizeLimitExceededException, DirectoryNotEmptyException, IncorrectAmountImagesToUploadException {
        Item item = new Item();
        Mockito.doReturn(true).when(itemChecker).wereImagesLoaded(Mockito.anyList(), Mockito.anyDouble());

        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        boolean bool = itemService.isItemSaved(item, new ArrayList<>(List.of(firstFile)));

        Assertions.assertTrue(bool);
        Mockito.verify(itemChecker).wereImagesLoaded(Mockito.anyList(), Mockito.anyDouble());
        Mockito.verify(itemRepository).save(item);
    }

    @Test
    void isItemSaved_FailedTest() throws FileSizeLimitExceededException, DirectoryNotEmptyException, IncorrectAmountImagesToUploadException {
        Item item = new Item();
        Mockito.doReturn(false).when(itemChecker).wereImagesLoaded(Mockito.anyList(), Mockito.anyDouble());

        boolean bool = itemService.isItemSaved(item, new ArrayList<>());

        Assertions.assertFalse(bool);
        Mockito.verify(itemRepository, Mockito.times(0)).save(item);
    }

    @Test
    void isItemUpdate_FailedTest() {
        Item item = new Item();
        Mockito.doReturn(Optional.of(item)).when(itemRepository).findById(Mockito.anyInt());
        Mockito.doReturn(false).when(itemChecker).wereImagesLoaded(Mockito.anyList(), Mockito.anyDouble());

        boolean isItemUpdate = itemService.isItemUpdate(1, item, new ArrayList<>());

        Assertions.assertFalse(isItemUpdate);
        Mockito.verify(itemChecker).wereImagesLoaded(Mockito.anyList(), Mockito.anyDouble());
        Mockito.verify(itemRepository).findById(Mockito.anyInt());
    }

    @Test
    void isItemUpdate_ExceptionTest() throws FileSizeLimitExceededException {
        Item item = new Item();
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.doReturn(Optional.of(item)).when(itemRepository).findById(Mockito.anyInt());
        Mockito.doThrow(FileSizeLimitExceededException.class).when(itemChecker).checkImageSize(multipartFile);

        boolean isItemChecked = itemService.isItemUpdate(1, item, new ArrayList<>());

        Assertions.assertFalse(isItemChecked);
        Mockito.verify(itemRepository).findById(1);
    }

    @Test
    void isItemUpdate_SuccessTest() {
        Item item = new Item();
        Mockito.doReturn(Optional.of(item)).when(itemRepository).findById(Mockito.anyInt());
        Mockito.doReturn(true).when(itemChecker).wereImagesLoaded(Mockito.anyList(), Mockito.anyDouble());
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());


        boolean isItemChecked = itemService.isItemUpdate(1, item, new ArrayList<>(List.of(firstFile)));

        Assertions.assertTrue(isItemChecked);
        Mockito.verify(itemChecker).wereImagesLoaded(Mockito.anyList(), Mockito.anyDouble());
        Mockito.verify(itemRepository).findById(Mockito.anyInt());
    }

    @Test
    void deleteItem() throws IOException {
        itemService.deleteItem(new Item());
        Mockito.verify(itemRepository).delete(new Item());
    }

    @Test
    void findItemsByNameOrderBySomething_IncreaseTest() {
        Mockito.doReturn(new ArrayList<>()).when(itemSorting).getCorrectSortedItemList(new ArrayList<>());
        Mockito.doReturn(new ArrayList<>()).when(itemRepository).findByItemNameContainingIgnoreCaseOrderByItemPrise("something");

        itemService.findItemsByNameOrderBySomething("something", "increase");

        Mockito.verify(itemRepository).findByItemNameContainingIgnoreCaseOrderByItemPrise("something");
        Mockito.verify(itemSorting).getCorrectSortedItemList(new ArrayList<>());
    }

    @Test
    void findItemsByNameOrderBySomething_DecreaseTest() {
        Mockito.doReturn(new ArrayList<>()).when(itemSorting).getCorrectSortedItemList(new ArrayList<>());
        Mockito.doReturn(new ArrayList<>()).when(itemRepository).findByItemNameContainingIgnoreCaseOrderByItemPriseDesc("something");

        itemService.findItemsByNameOrderBySomething("something", "decrease");

        Mockito.verify(itemRepository).findByItemNameContainingIgnoreCaseOrderByItemPriseDesc("something");
        Mockito.verify(itemSorting).getCorrectSortedItemList(new ArrayList<>());
    }

    @Test
    void findItemsByNameOrderBySomething_PopularTest() {
        Mockito.doReturn(new ArrayList<>()).when(itemSorting).getCorrectSortedItemList(new ArrayList<>());
        Mockito.doReturn(new ArrayList<>()).when(itemRepository).findByItemNameContainingIgnoreCaseOrderAndSortByAmountOfPurchaseRequest("something");

        itemService.findItemsByNameOrderBySomething("something", "popular");

        Mockito.verify(itemRepository).findByItemNameContainingIgnoreCaseOrderAndSortByAmountOfPurchaseRequest("something");
        Mockito.verify(itemSorting).getCorrectSortedItemList(new ArrayList<>());
    }

    @Test
    void findItemsByNameOrderBySomething_DefaultTest() {
        Mockito.doReturn(new ArrayList<>()).when(itemSorting).getCorrectSortedItemList(new ArrayList<>());
        Mockito.doReturn(new ArrayList<>()).when(itemRepository).findByItemNameContainingIgnoreCaseOrderByItemDateOfCreateDesc("something");

        itemService.findItemsByNameOrderBySomething("something", "default");

        Mockito.verify(itemRepository).findByItemNameContainingIgnoreCaseOrderByItemDateOfCreateDesc("something");
        Mockito.verify(itemSorting).getCorrectSortedItemList(new ArrayList<>());
    }
}