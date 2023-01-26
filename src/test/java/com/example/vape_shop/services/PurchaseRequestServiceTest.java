package com.example.vape_shop.services;

import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.models.PurchaseRequest;
import com.example.vape_shop.repositories.PurchaseRequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PurchaseRequestServiceTest {

    @Autowired
    private PurchaseRequestService purchaseRequestService;

    @MockBean
    private PurchaseRequestRepository purchaseRequestRepository;
    @MockBean
    private ItemService itemService;
    @MockBean
    private ManService manService;

    @Test
    void save() {
        Item item = new Item();
        Man man = new Man();
        Mockito.doReturn(null).when(purchaseRequestRepository).save(new PurchaseRequest());
        purchaseRequestService.save(item, man);
        Mockito.verify(purchaseRequestRepository).save(Mockito.any());
    }

    @Test
    void findByMan() {
        Man man = new Man();
        List<PurchaseRequest> purchaseRequestsActual = new ArrayList<>(List.of(new PurchaseRequest()));
        Mockito.doReturn(purchaseRequestsActual).when(purchaseRequestRepository).findByMan(man);

        List<PurchaseRequest> purchaseRequests = purchaseRequestService.findByMan(man);

        Assertions.assertNotNull(purchaseRequests);
        Mockito.verify(purchaseRequestRepository).findByMan(man);
    }

    @Test
    void findByItem() {
        Item item = new Item();
        List<PurchaseRequest> purchaseRequestsActual = new ArrayList<>(List.of(new PurchaseRequest()));
        Mockito.doReturn(purchaseRequestsActual).when(purchaseRequestRepository).findByItem(item);

        List<PurchaseRequest> purchaseRequests = purchaseRequestService.findByItem(item);

        Assertions.assertNotNull(purchaseRequests);
        Mockito.verify(purchaseRequestRepository).findByItem(item);
    }

    @Test
    void getPeopleWhichToBookThisItem() {
        Item item = new Item();
        PurchaseRequest purchaseRequest1 = new PurchaseRequest();
        purchaseRequest1.setMan(new Man());
        PurchaseRequest purchaseRequest2 = new PurchaseRequest();
        purchaseRequest2.setMan(new Man());
        item.setPurchaseRequests(List.of(purchaseRequest1, purchaseRequest2));
        List<PurchaseRequest> purchaseRequestsActual = new ArrayList<>(List.of(purchaseRequest1, purchaseRequest2));
        Mockito.doReturn(purchaseRequestsActual).when(purchaseRequestRepository).findByItem(item);

        List<Man> mans = purchaseRequestService.getPeopleWhichToBookThisItem(item);

        Assertions.assertNotNull(mans);
        Mockito.verify(purchaseRequestRepository).findByItem(item);
    }

    @Test
    void delete() {
        PurchaseRequest purchaseRequest1 = new PurchaseRequest();
        PurchaseRequest purchaseRequest2 = new PurchaseRequest();
        Man man = new Man();
        man.setPurchaseRequests(Set.of(purchaseRequest1, purchaseRequest2));
        Item item = new Item();
        item.setPurchaseRequests(List.of(purchaseRequest1, purchaseRequest2));
        purchaseRequest1.setMan(man);
        purchaseRequest1.setItem(item);
        List<PurchaseRequest> purchaseRequestsActual = new ArrayList<>(List.of(purchaseRequest1, purchaseRequest2));

        Mockito.doReturn(purchaseRequestsActual).when(purchaseRequestRepository).findByMan(man);
        Mockito.doReturn(purchaseRequestsActual).when(purchaseRequestRepository).findByItem(item);
        Mockito.doNothing().when(purchaseRequestRepository).delete(purchaseRequest1);

        purchaseRequestService.delete(purchaseRequest1);

        Assertions.assertEquals(Set.of(purchaseRequest2), man.getPurchaseRequests());
        Assertions.assertEquals(List.of(purchaseRequest2), item.getPurchaseRequests());
        Assertions.assertTrue(item.isSold());

        Mockito.verify(purchaseRequestRepository).delete(purchaseRequest1);
        Mockito.verify(itemService).save(item);
        Mockito.verify(manService).saveMan(man);
    }

    @Test
    void deleteAllConnectionsFromPurchaseList() {
        PurchaseRequest purchaseRequest1 = new PurchaseRequest();
        PurchaseRequest purchaseRequest2 = new PurchaseRequest();
        Man man = new Man();
        man.setPurchaseRequests(Set.of(purchaseRequest1, purchaseRequest2));
        Item item = new Item();
        item.setPurchaseRequests(List.of(purchaseRequest1, purchaseRequest2));
        purchaseRequest1.setMan(man);
        purchaseRequest1.setItem(item);
        List<PurchaseRequest> purchaseRequestsActual = new ArrayList<>(List.of(purchaseRequest1, purchaseRequest2));

        Mockito.doReturn(purchaseRequestsActual).when(purchaseRequestRepository).findByMan(man);
        Mockito.doReturn(purchaseRequestsActual).when(purchaseRequestRepository).findByItem(item);

        purchaseRequestService.deleteAllConnectionsFromPurchaseList(purchaseRequestsActual);

        Assertions.assertEquals(Set.of(purchaseRequest2), man.getPurchaseRequests());
        Assertions.assertEquals(List.of(purchaseRequest2), item.getPurchaseRequests());
        Assertions.assertTrue(item.isSold());

        Mockito.verify(purchaseRequestRepository).delete(purchaseRequest1);
        Mockito.verify(itemService).save(item);
        Mockito.verify(manService).saveMan(man);

    }
}