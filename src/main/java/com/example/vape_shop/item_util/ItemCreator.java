package com.example.vape_shop.item_util;

import com.example.vape_shop.models.Item;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;

@Component
public class ItemCreator {

    private void toFillAvatarsIntoItem(Item item, List<MultipartFile> imagesForLoading, double randomValueForOriginalNameOfImages) {
        int amountImages = imagesForLoading.size();
        String pathToDirectoryWithImages = "/css1/ImagesForItems/";
        if (amountImages == 1) {
            item.setItemAvatar(pathToDirectoryWithImages + randomValueForOriginalNameOfImages + imagesForLoading.get(0).getOriginalFilename());
        }
        else if (amountImages == 2) {
            item.setItemAvatar(pathToDirectoryWithImages + randomValueForOriginalNameOfImages + imagesForLoading.get(0).getOriginalFilename());
            item.setItemAvatar2(pathToDirectoryWithImages + randomValueForOriginalNameOfImages + imagesForLoading.get(1).getOriginalFilename());
        }
        else {
            item.setItemAvatar(pathToDirectoryWithImages + randomValueForOriginalNameOfImages + imagesForLoading.get(0).getOriginalFilename());
            item.setItemAvatar2(pathToDirectoryWithImages + randomValueForOriginalNameOfImages + imagesForLoading.get(1).getOriginalFilename());
            item.setItemAvatar3(pathToDirectoryWithImages + randomValueForOriginalNameOfImages + imagesForLoading.get(2).getOriginalFilename());
        }
    }

    public void toFillGapsIntoNewItemFromOldItem(Item oldItem, Item newItem) {
        newItem.setItemId(oldItem.getItemId());
        newItem.setMan(oldItem.getMan());
        newItem.setItemDateOfCreate(oldItem.getItemDateOfCreate());
        newItem.setItemDateOfLastUpdate(new Date());
        newItem.setItemChecked("NO");
    }

    public void prepareItemForSaving(Item item, List<MultipartFile> imagesForLoading, double randomValueForOriginalNameOfImages) {
        toFillAvatarsIntoItem(item, imagesForLoading, randomValueForOriginalNameOfImages);
        item.setItemDateOfCreate(new Date());
        item.setItemChecked("NO");
    }
}
