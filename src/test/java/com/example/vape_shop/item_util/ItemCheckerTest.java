package com.example.vape_shop.item_util;

import com.example.vape_shop.util.ImageWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ItemCheckerTest {

    @MockBean
    private ImageWorker imageWorker;

    @InjectMocks
    private ItemChecker itemChecker;

    @Test
    void wereImagesLoadedFalseTest() throws IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        List<MultipartFile> arrayLists = new ArrayList<>();
        arrayLists.add(file);

        Mockito.doThrow(IOException.class).when(imageWorker)
                .downloadImagesForItemIntoTheProject(arrayLists, 1.0);

        Assertions.assertFalse(itemChecker.wereImagesLoaded(List.of(file), 2.0));
        Mockito.verify(imageWorker, Mockito.times(0))
                .downloadImagesForItemIntoTheProject(Mockito.anyList(), Mockito.anyDouble());
    }

    @Test
    void wereImagesLoadedDownloadedMinimumOneImage() throws IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        List<MultipartFile> arrayLists = new ArrayList<>();
        arrayLists.add(file);

        Mockito.doThrow(IOException.class).when(imageWorker)
                .downloadImagesForItemIntoTheProject(arrayLists, 1.0);

        Assertions.assertFalse(itemChecker.wereImagesLoaded(List.of(file), 2.0));
        Mockito.verify(imageWorker, Mockito.times(0))
                .downloadImagesForItemIntoTheProject(Mockito.anyList(), Mockito.anyDouble());
    }

    @Test
    void wereImagesLoadedImageSizeTest() throws IOException {
        byte[] bytes = new byte[10000000];

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                bytes
        );

        List<MultipartFile> arrayLists = new ArrayList<>();
        arrayLists.add(file);

        Mockito.doThrow(IOException.class).when(imageWorker)
                .downloadImagesForItemIntoTheProject(arrayLists, 1.0);

        Assertions.assertFalse(itemChecker.wereImagesLoaded(List.of(file), 2.0));
        Mockito.verify(imageWorker, Mockito.times(0))
                .downloadImagesForItemIntoTheProject(Mockito.anyList(), Mockito.anyDouble());
    }

    @Test
    void wereImagesLoadedListImagesSizeTest() throws IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        MockMultipartFile file2 = file;
        MockMultipartFile file3 = file;
        MockMultipartFile file4 = file;

        List<MultipartFile> arrayLists = new ArrayList<>();
        arrayLists.add(file);

        Mockito.doThrow(IOException.class).when(imageWorker)
                .downloadImagesForItemIntoTheProject(arrayLists, 1.0);

        Assertions.assertFalse(itemChecker
                .wereImagesLoaded(List.of(file, file2, file3, file4), 2.0));
        Assertions.assertFalse(itemChecker
                .wereImagesLoaded(List.of(), 2.0));
        Mockito.verify(imageWorker, Mockito.times(0))
                .downloadImagesForItemIntoTheProject(Mockito.anyList(), Mockito.anyDouble());
    }
}