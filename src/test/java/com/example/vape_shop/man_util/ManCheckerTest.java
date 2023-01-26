package com.example.vape_shop.man_util;

import com.example.vape_shop.models.Man;
import com.example.vape_shop.util.ImageWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ManCheckerTest {

    @Autowired
    private ManChecker manChecker;

    @MockBean
    private ImageWorker imageWorker;

    @Test
    void isManPrepareForUpdating_SuccessTest() throws IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "file.jpeg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        Man man = new Man();
        boolean isPrepare = manChecker.isManPrepareForUpdating(man, file, 20.0);

        Assertions.assertTrue(isPrepare);
        Mockito.verify(imageWorker).setNewImageForMan(man, file, 20.0);
        Mockito.verify(imageWorker).downloadImageForManIntoTheProject(file, 20.0);
    }

    @Test
    void isManPrepareForUpdating_DirectoryNotEmptyExceptionTest() throws IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        Man man = new Man();
        boolean isPrepare = manChecker.isManPrepareForUpdating(man, file, 20.0);
        Assertions.assertFalse(isPrepare);
    }

    @Test
    void isManPrepareForUpdating_FileSizeLimitExceededExceptionTest() throws IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "file.jpeg",
                MediaType.TEXT_PLAIN_VALUE,
                new byte[124424333]
        );
        Man man = new Man();
        boolean isPrepare = manChecker.isManPrepareForUpdating(man, file, 20.0);
        Assertions.assertFalse(isPrepare);
    }

    @Test
    void isManPrepareForUpdating_ImageWorkerExceptionTest() throws IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "file.jpeg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, world".getBytes()
        );
        Man man = new Man();
        Mockito.doThrow(IOException.class).when(imageWorker).downloadImageForManIntoTheProject(file, 20.0);
        boolean isPrepare = manChecker.isManPrepareForUpdating(man, file, 20.0);
        Assertions.assertFalse(isPrepare);
    }
}