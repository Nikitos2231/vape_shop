package com.example.vape_shop.item_util;

import com.example.vape_shop.util.ImageWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.List;
import java.util.Objects;

@Component
public class ItemChecker {

    private static final Logger logger = LogManager.getLogger(ItemChecker.class);
    private final ImageWorker imageWorker;

    @Autowired
    public ItemChecker(ImageWorker imageWorker) {
        this.imageWorker = imageWorker;
    }

    private boolean areImagesDownloadedIntoTheProject(List<MultipartFile> imagesForLoading, double randomValueForOriginalNameOfImages) {
        try {
            imageWorker.downloadImagesForItemIntoTheProject(imagesForLoading, randomValueForOriginalNameOfImages);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean areImagesCorrect(List<MultipartFile> imagesForLoading) {
        try {
            for (MultipartFile image : imagesForLoading) {
                checkImageSize(image);
                checkDownloadImage(image);
            }
        } catch (DirectoryNotEmptyException | FileSizeLimitExceededException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void checkDownloadImage(MultipartFile image) throws DirectoryNotEmptyException {
        if (Objects.requireNonNull(image.getOriginalFilename()).isEmpty()) {
            logger.warn("Пользователь не выбрал ни одной фотографии для вставки");
            throw new DirectoryNotEmptyException("Didn't download the file");
        }
    }

    public void checkImageSize(MultipartFile image) throws FileSizeLimitExceededException {
        if (image.getSize() >= 1048576) {
            logger.warn("Пользователь попытался загрузить фотографию для карточки с размером: {}байт", image.getSize());
            throw new FileSizeLimitExceededException("The file is too much size", image.getSize(), 1048576);
        }
    }

    public boolean wereImagesLoaded(List<MultipartFile> imagesForLoading, double randomValueForOriginalNameOfImages) {
        if (!isValidAmountImagesToUpload(imagesForLoading.size())) {
            return false;
        }
        if (!areImagesCorrect(imagesForLoading)) {
            return false;
        }
        return areImagesDownloadedIntoTheProject(imagesForLoading, randomValueForOriginalNameOfImages);
    }

    private boolean isValidAmountImagesToUpload(int amountImages) {
        return (amountImages > 0) && (amountImages < 4);
    }
}
