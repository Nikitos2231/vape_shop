package com.example.vape_shop.services;

import com.example.vape_shop.man_util.ManChecker;
import com.example.vape_shop.models.*;
import com.example.vape_shop.repositories.ManRepository;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ManServiceTest {

//    private static final int id = 2;

    @MockBean
    private ManRepository manRepository;

    @MockBean
    private ManChecker manChecker;

    @Autowired
    private ManService manService;

    @Test
    void findOne() {
        final Man man = mock(Man.class);
        when(manRepository.findById(anyInt())).thenReturn(Optional.ofNullable(man));

        Man actual = manService.findOne(anyInt());

        assertNotNull(actual);
        assertEquals(man, actual);
        verify(manRepository, Mockito.times(1)).findById(anyInt());
    }

    @Test
    void findAll() {
        final Man man2 = Mockito.mock(Man.class);
        final Man man1 = Mockito.mock(Man.class);
        List<Man> mans = new ArrayList<>();
        mans.add(man1);
        mans.add(man2);

        when(manRepository.findAll()).thenReturn(mans);

        final List<Man> actual = manService.findAll();

        assertNotNull(actual);
        assertEquals(actual, mans);
        verify(manRepository).findAll();
    }

    @Test
    void save() {
        Man man = Mockito.mock(Man.class);

        manService.saveMan(man);

        verify(manRepository).save(man);
    }

    @Test
    void getManByEmail_SuccessTest() {
        Man man = Mockito.mock(Man.class);

        when(manRepository.findByUserEmail(anyString())).thenReturn(Optional.ofNullable(man));
        Man actual = manService.getManByEmail(anyString());

        assertNotNull(actual);
        assertEquals(man, actual);
        verify(manRepository).findByUserEmail(anyString());
    }

    @Test
    void getManByEmail_FailedTest() {
        Mockito.doReturn(Optional.empty()).when(manRepository).findByUserEmail(anyString());
        try {
            manService.loadUserByUsername(anyString());
        }
        catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found", e.getMessage());
        }

    }

    @Test
    void findManByItem() {
        Item item = new Item(2);
        Man man = new Man();
        man.setItems(Set.of(item));
        Mockito.doReturn(man).when(manRepository).findByItems(item);

        Man expected = manService.findManByItem(item);

        Assertions.assertEquals(expected, man);
        Mockito.verify(manRepository).findByItems(item);
    }

    @Test
    void inheritAttributesForUpdatedManFromOldMan() {
        Man oldMan = new Man(1, "test@mail.ru", "Nikita", "Porshennikov", new Date(), "123",
                "ROLE_ADMIN", "111", 5, "file.jpeg",
                "telega", new HashSet<Item>(), new HashSet<Comment>(), new HashSet<PurchaseRequest>(), new ArrayList<Item>(),
                new HashSet<Feedback>(), new HashSet<Feedback>());
        Man newMan = new Man();
        Mockito.doReturn(Optional.of(oldMan)).when(manRepository).findById(1);

        manService.inheritAttributesForUpdatedManFromOldMan(newMan, 1);

        Assertions.assertEquals(1, newMan.getUserId());
        Assertions.assertEquals("test@mail.ru", newMan.getUserEmail());
        Assertions.assertEquals("123", newMan.getUserPassword());
        Assertions.assertEquals("ROLE_ADMIN", newMan.getUserRole());
        Assertions.assertEquals("111", newMan.getActivationCode());
        Assertions.assertEquals(5, newMan.getUserCountStars());
        Assertions.assertEquals("file.jpeg", newMan.getAvatar());

        Mockito.verify(manRepository).findById(1);
    }

    @Test
    void isManUpdated() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("gergerg", new byte[143]);
        Man man = new Man();
        Mockito.doReturn(false).when(manChecker).isManPrepareForUpdating(man, multipartFile, 2.0);

        boolean expected = manService.isManUpdated(man, multipartFile);

        Assertions.assertFalse(expected);
    }

    @Test
    void getFoundPeople_EmptyTest() {
        List<Man> manList = manService.getFoundPeople("");
        Assertions.assertTrue(manList.isEmpty());
    }

    @Test
    void getFoundPeople_IdTest() {
        Man man = new Man();
        List<Man> mans = new ArrayList<>(List.of(man));
        Mockito.doReturn(Optional.of(man)).when(manRepository).findById(123);
        Mockito.doReturn(new ArrayList<>()).when(manRepository).findManByUserEmailContainingIgnoreCase(Mockito.anyString());

        List<Man> manList = manService.getFoundPeople("123");

        Assertions.assertFalse(manList.isEmpty());
        Assertions.assertEquals(manList, mans);
    }

    @Test
    void getFoundPeople_EmailTest() {
        Man man = new Man();
        Man man2 = new Man();
        List<Man> mans = new ArrayList<>(List.of(man, man2));
        Mockito.doReturn(Optional.of(man)).when(manRepository).findById(123);
        Mockito.doReturn(List.of(man2)).when(manRepository).findManByUserEmailContainingIgnoreCase(Mockito.anyString());

        List<Man> manList = manService.getFoundPeople("123");

        Assertions.assertFalse(manList.isEmpty());
        Assertions.assertEquals(manList, mans);
    }

}