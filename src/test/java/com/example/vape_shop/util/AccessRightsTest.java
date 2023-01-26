package com.example.vape_shop.util;

import com.example.vape_shop.item_util.ItemSorting;
import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.services.ItemService;
import com.example.vape_shop.services.ManService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith({MockitoExtension.class})
class AccessRightsTest {

    @Autowired
    private AccessRights accessRights;

    @MockBean
    private ItemService itemService;
    @MockBean
    private ItemSorting itemSorting;
    @MockBean
    private PutModeler putModeler;
    @MockBean
    private ManService manService;


    @Test
    void fillModelForItemPage_AuthenticatedMan() {
        Model model = new ConcurrentModel();
        Man man = new Man();
        man.setUserId(1);
        man.setUserRole("ROLE_USER");
        Item item = new Item();
        man.setItems(Set.of(item));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(true).when(manService).isManAuth(authentication);
        Mockito.doReturn(man).when(manService).getEnteredMan();

        accessRights.fillModelForItemPage(authentication, model, man, item);

        Assertions.assertEquals(model.getAttribute("isAnonymous"), false);
        Assertions.assertEquals(model.getAttribute("item"), item);
        Assertions.assertEquals(model.getAttribute("man"), man);
        Assertions.assertEquals(model.getAttribute("enteredMan"), man);
        Assertions.assertEquals(model.getAttribute("canEstimate"), false);
        Assertions.assertEquals(model.getAttribute("isItemSold"), false);
    }

    @Test
    void fillModelForItemPage_NotAuthenticatedMan() {
        Model model = new ConcurrentModel();
        Man man = new Man();
        man.setUserId(1);
        man.setUserRole("ROLE_USER");
        Item item = new Item();
        man.setItems(Set.of(item));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(false).when(manService).isManAuth(authentication);

        accessRights.fillModelForItemPage(authentication, model, man, item);

        Assertions.assertEquals(model.getAttribute("item"), item);
        Assertions.assertEquals(model.getAttribute("man"), man);
    }

    @Test
    void fillModelForProfilePage_AuthTest() {
        Model model = new ConcurrentModel();
        Man man = new Man();
        Man man2 = new Man();
        man2.setUserId(2);

        man.setUserId(1);
        man.setUserRole("ROLE_USER");
        Item allItems1 = new Item(1);
        Item allItems2 = new Item(2);
        Item allItems3 = new Item(3);
        allItems3.setBuyer(man2);
        allItems3.setSold(true);
        man.setItems(Set.of(allItems1, allItems2, allItems3));
        man.setBoughtItems(List.of(allItems1, allItems2));
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(true).when(manService).isManAuth(authentication);
        Mockito.doReturn(man.getBoughtItems()).when(itemSorting).sortByDateBoughtItemsByMan(Mockito.anyList());
        Mockito.doReturn(List.of(allItems3)).when(itemService).getItemsForManWhereItemIsSoldEqualsYes(man);
        Mockito.doReturn(man2).when(manService).getEnteredMan();

        accessRights.fillModelForProfilePage(authentication, model, man);

        Assertions.assertEquals(model.getAttribute("isAnonymous"), false);
        Assertions.assertNotNull(model.getAttribute("items"));
        Assertions.assertEquals(model.getAttribute("canEstimate"), true);
        Assertions.assertEquals(model.getAttribute("boughtItems"), man.getBoughtItems());
        Assertions.assertEquals(model.getAttribute("soldItems"), List.of(allItems3));

        Mockito.verify(manService).isManAuth(authentication);
        Mockito.verify(itemSorting).sortByDateBoughtItemsByMan(Mockito.anyList());
        Mockito.verify(itemService).getItemsForManWhereItemIsSoldEqualsYes(man);
        Mockito.verify(manService).getEnteredMan();
    }

    @Test
    void fillModelForProfilePage_NotAuthTest() {
        Model model = new ConcurrentModel();
        Man man = new Man();

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.doReturn(false).when(manService).isManAuth(authentication);

        accessRights.fillModelForProfilePage(authentication, model, man);

        Assertions.assertEquals(model.getAttribute("isAnonymous"), false);
        Mockito.verify(manService).isManAuth(authentication);
    }

}