package com.example.vape_shop.services;

import com.example.vape_shop.exceptions.IncorrectAmountImagesToUploadException;
import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.repositories.ItemRepository;
import com.example.vape_shop.util.ImageWorker;
import com.example.vape_shop.item_util.ItemChecker;
import com.example.vape_shop.item_util.ItemCreator;
import com.example.vape_shop.item_util.ItemSorting;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.util.*;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemChecker itemChecker;
    private final ItemCreator itemCreator;
    private final ItemSorting itemSorting;
    private final ImageWorker imageWorker;

    @Autowired
    public ItemService(ItemRepository itemRepository, ItemChecker itemChecker, ItemCreator itemCreator, ItemSorting itemSorting, ImageWorker imageWorker) {
        this.itemRepository = itemRepository;
        this.itemChecker = itemChecker;
        this.itemCreator = itemCreator;
        this.itemSorting = itemSorting;
        this.imageWorker = imageWorker;
    }

    @Transactional
    public void deleteItem(Item item) throws IOException {
        imageWorker.clearOldImages(getListAvatarsFromItem(item));
        itemRepository.delete(item);
    }
    @Transactional
    public boolean isItemUpdate(int oldItemId, Item newItem, List<MultipartFile> imagesForLoading) {
        itemCreator.toFillGapsIntoNewItemFromOldItem(findById(oldItemId), newItem);
        try {
            if (!isItemSaved(newItem, imagesForLoading)) {
               return false;
            }
        } catch (IncorrectAmountImagesToUploadException | FileSizeLimitExceededException | DirectoryNotEmptyException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Transactional
    public boolean isItemSaved(Item item, List<MultipartFile> imagesForLoading) throws IncorrectAmountImagesToUploadException, FileSizeLimitExceededException, DirectoryNotEmptyException {
        double randomValueForOriginalNameOfImages = Math.random() * 100;
        if (!itemChecker.wereImagesLoaded(imagesForLoading, randomValueForOriginalNameOfImages)) {
            return false;
        }
        itemCreator.prepareItemForSaving(item, imagesForLoading, randomValueForOriginalNameOfImages);
        itemRepository.save(item);
        return true;
    }
    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }
    @Transactional
    public void activatedCard(Item item) {
        item.setItemChecked("YES");
        itemRepository.save(item);
    }
    public List<Item> findAllByMan(Man man) {
        return itemRepository.findAllByMan(man);
    }

    public Item findById(int id) {
        return itemRepository.findById(id).orElse(null);
    }
    public List<Item> findItemsByNameOrderBySomething(String name, String sort) {
        List<Item> items;
        switch (sort) {
            case "increase" -> {
                items = itemRepository.findByItemNameContainingIgnoreCaseOrderByItemPrise(name);
            }
            case "decrease" -> {
                items = itemRepository.findByItemNameContainingIgnoreCaseOrderByItemPriseDesc(name);
            }
            case "popular" -> {
                items = itemRepository.findByItemNameContainingIgnoreCaseOrderAndSortByAmountOfPurchaseRequest(name);
            }
            default -> {
                items = itemRepository.findByItemNameContainingIgnoreCaseOrderByItemDateOfCreateDesc(name.toLowerCase());
            }
        }
        return itemSorting.getCorrectSortedItemList(items);
    }
    public List<Item> getItemsForManWhereItemIsSoldEqualsYes(Man man) {
        return itemSorting.getFilteredAndUniqAndSortByDateItemsWhereAttrIsSoldEqualYes(findAllByMan(man));
    }
    public List<Item> findAllByManAndItemChecked(int idOwner, String itemChecked) {
        return itemSorting.removeTheSameElementsIntoItemList(itemRepository.findAllByManAndItemCheckedAndIsSoldEqualFalse(idOwner, itemChecked));
    }
    public List<Item> findAllByItemChecked(String itemChecked) {
        return itemRepository.findAllByItemChecked(itemChecked);
    }
    public List<String> getListAvatarsFromItem(Item item) {
        List<String> itemAvatars = new ArrayList<>();
        itemAvatars.add(item.getItemAvatar());
        itemAvatars.add(item.getItemAvatar2());
        itemAvatars.add(item.getItemAvatar3());
        return itemAvatars;
    }
    public List<Item> getCheckedItemsForPeopleSortedByDate(Man ownMan) {
        List<Item> items = findAllByManAndItemChecked(ownMan.getUserId(), "YES");
        return itemSorting.sortItemsByDate(items);
    }
    public List<Item> getNoCheckedItemsForPeopleSortedByDate(Man ownMan) {
        List<Item> items = findAllByManAndItemChecked(ownMan.getUserId(), "NO");
        return itemSorting.sortItemsByDate(items);
    }

}
