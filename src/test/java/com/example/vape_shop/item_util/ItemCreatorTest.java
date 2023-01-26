package com.example.vape_shop.item_util;

import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ItemCreatorTest {

    @InjectMocks
    private ItemCreator itemCreator;

    @Test
    void toFillGapsIntoNewItemFromOldItem() {
        Item oldItem = new Item();
        oldItem.setItemId(1);
        oldItem.setMan(new Man());
        oldItem.setItemDateOfCreate(new Date());
        oldItem.setItemDateOfLastUpdate(new Date());
        oldItem.setItemChecked("Yes");

        Item item = new Item();

        itemCreator.toFillGapsIntoNewItemFromOldItem(oldItem, item);
        Assertions.assertEquals(item.getItemId(), 1);
        Assertions.assertNotNull(item.getMan());
        Assertions.assertNotNull(item.getItemDateOfCreate());
        Assertions.assertNotNull(item.getItemDateOfLastUpdate());
        Assertions.assertEquals(item.getItemChecked(), "NO");
    }

    @Test
    void prepareItemForSaving() {
        Item item = new Item();
        MultipartFile multipartFiles = Mockito.mock(MultipartFile.class);
        itemCreator.prepareItemForSaving(item, Collections.singletonList(multipartFiles), 24.0);
        Assertions.assertNotNull(item.getItemDateOfCreate());
        Assertions.assertNotNull(item.getItemChecked());
        Assertions.assertEquals(item.getItemChecked(), "NO");
    }

    @Test
    void prepareItemForSavingOneFile() {
        Item item = new Item();
        MultipartFile multipartFiles = Mockito.mock(MultipartFile.class);
        itemCreator.prepareItemForSaving(item, Collections.singletonList(multipartFiles), 24.0);
        Assertions.assertNotNull(item.getItemAvatar());
        Assertions.assertNull(item.getItemAvatar2());
        Assertions.assertNull(item.getItemAvatar3());
    }

    @Test
    void prepareItemForSavingTwoFiles() {
        Item item = new Item();
        MultipartFile multipartFile1 = Mockito.mock(MultipartFile.class);
        MultipartFile multipartFile2 = Mockito.mock(MultipartFile.class);
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(multipartFile1);
        multipartFiles.add(multipartFile2);
        itemCreator.prepareItemForSaving(item, multipartFiles, 24.0);
        Assertions.assertNotNull(item.getItemAvatar());
        Assertions.assertNotNull(item.getItemAvatar2());
        Assertions.assertNull(item.getItemAvatar3());
    }

    @Test
    void prepareItemForSavingThreeFiles() {
        Item item = new Item();
        MultipartFile multipartFile1 = Mockito.mock(MultipartFile.class);
        MultipartFile multipartFile2 = Mockito.mock(MultipartFile.class);
        MultipartFile multipartFile3 = Mockito.mock(MultipartFile.class);
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(multipartFile1);
        multipartFiles.add(multipartFile2);
        multipartFiles.add(multipartFile3);
        itemCreator.prepareItemForSaving(item, multipartFiles, 24.0);
        Assertions.assertNotNull(item.getItemAvatar());
        Assertions.assertNotNull(item.getItemAvatar2());
        Assertions.assertNotNull(item.getItemAvatar3());
    }
}