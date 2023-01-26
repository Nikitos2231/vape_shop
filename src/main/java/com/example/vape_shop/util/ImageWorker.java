package com.example.vape_shop.util;

import com.example.vape_shop.models.Man;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Component
public class ImageWorker {

    public void downloadImagesForItemIntoTheProject(List<MultipartFile> imagesForLoading, double randomValueForOriginalNameOfImages) throws IOException {
        for (MultipartFile image : imagesForLoading) {
            Path copyLocation2 = Paths.get("src/main/resources/static/css1/ImagesForItems/" + randomValueForOriginalNameOfImages + image.getOriginalFilename());
            Path copyLocation = Paths.get("target/classes/static/css1/ImagesForItems/" + randomValueForOriginalNameOfImages + image.getOriginalFilename());
            Files.copy(image.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(image.getInputStream(), copyLocation2, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void clearOldImages(List<String> imageAvatars) throws IOException {
        for (String imageAvatar : imageAvatars) {
            if (imageAvatar != null) {
                Files.delete(Path.of("src/main/resources/static" + imageAvatar));
                Files.delete(Path.of("target/classes/static" + imageAvatar));
            }
        }
    }

    public void downloadImageForManIntoTheProject(MultipartFile imageForMan, double randomValueForDifferentNamingImages) throws IOException {
        Path copyLocation2 = Paths.get("src/main/resources/static/css1/ImagesForPeople/" + randomValueForDifferentNamingImages + imageForMan.getOriginalFilename());
        Path copyLocation = Paths.get("target/classes/static/css1/ImagesForPeople/" + randomValueForDifferentNamingImages + imageForMan.getOriginalFilename());
        Files.copy(imageForMan.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(imageForMan.getInputStream(), copyLocation2, StandardCopyOption.REPLACE_EXISTING);
    }

    public void setNewImageForMan(Man manForUpdating, MultipartFile imageForMan, double randomValueForDifferentNamingImages) throws IOException {
        String defaultImageForPeople = "https://e7.pngegg.com/pngimages/831/88/png-clipart-user-profile-computer-icons-user-interface-mystique-miscellaneous-user-interface-design.png";
        if (!manForUpdating.getAvatar().equals(defaultImageForPeople)) {
            Files.delete(Path.of("src/main/resources/static" + manForUpdating.getAvatar()));
            Files.delete(Path.of("target/classes/static" + manForUpdating.getAvatar()));
        }
        manForUpdating.setAvatar("/css1/imagesForPeople/" + randomValueForDifferentNamingImages  + imageForMan.getOriginalFilename());
    }


}
