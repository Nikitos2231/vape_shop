package com.example.vape_shop.man_util;

import com.example.vape_shop.models.Man;
import com.example.vape_shop.util.ImageWorker;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.util.Objects;

@Component
public class ManChecker {

    private final ImageWorker imageWorker;

    @Autowired
    public ManChecker(ImageWorker imageWorker) {
        this.imageWorker = imageWorker;
    }

    private void isImageForManChosen(MultipartFile imageForMan) throws DirectoryNotEmptyException {
        if (Objects.requireNonNull(imageForMan.getOriginalFilename()).isEmpty()) {
            throw new DirectoryNotEmptyException("Didn't download the file");
        }
    }

    private void haveImageForManAvailableSize(MultipartFile imageForMan) throws FileSizeLimitExceededException {
        if (imageForMan.getSize() >= 1048576) {
            throw new FileSizeLimitExceededException("The file is too much size", imageForMan.getSize(), 1048576);
        }
    }

    private boolean isImageSuccessfullyLoaded(MultipartFile imageForMan, double randomValueForDifferentNamingImages) {
        try {
            imageWorker.downloadImageForManIntoTheProject(imageForMan, randomValueForDifferentNamingImages);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean isImageForManLoaded(MultipartFile imageForMan, double randomValueForDifferentNamingImages) throws IOException {
        isImageForManChosen(imageForMan);
        haveImageForManAvailableSize(imageForMan);
        return isImageSuccessfullyLoaded(imageForMan, randomValueForDifferentNamingImages);
    }

    public boolean isManPrepareForUpdating(Man manForUpdating, MultipartFile imageForMan, double randomValueForDifferentNamingImages) throws IOException {
        try {
            if (!isImageForManLoaded(imageForMan, randomValueForDifferentNamingImages)) {
                return false;
            }
        } catch (DirectoryNotEmptyException | FileSizeLimitExceededException e) {
            e.printStackTrace();
            return false;
        }
        imageWorker.setNewImageForMan(manForUpdating, imageForMan, randomValueForDifferentNamingImages);
        return true;
    }

}
