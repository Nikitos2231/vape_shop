package com.example.vape_shop.services;

import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.models.PurchaseRequest;
import com.example.vape_shop.repositories.PurchaseRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class PurchaseRequestService {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final ItemService itemService;
    private final ManService manService;

    @Autowired
    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository, ItemService itemService, ManService manService) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.itemService = itemService;
        this.manService = manService;
    }

    @Transactional
    public void save(Item item, Man man) {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setItem(item);
        purchaseRequest.setMan(man);
        purchaseRequestRepository.save(purchaseRequest);
    }
    public List<PurchaseRequest> findByMan(Man man) {
        return purchaseRequestRepository.findByMan(man);
    }

    public List<PurchaseRequest> findByItem(Item item) {
        return purchaseRequestRepository.findByItem(item);
    }

    public List<Man> getPeopleWhichToBookThisItem(Item item) {
        List<PurchaseRequest> purchaseRequests = findByItem(item);
        List<Man> manList = new ArrayList<>();
        for (PurchaseRequest purchaseRequest : purchaseRequests) {
            manList.add(purchaseRequest.getMan());
        }
        return manList;
    }

    @Transactional
    public void delete(PurchaseRequest purchaseRequest) {
        Man man = purchaseRequest.getMan();
        Item item = purchaseRequest.getItem();
        man.setPurchaseRequests(new HashSet<>(getNewPurchaseListWithoutSelectedPurchaseRequest(man, purchaseRequest)));
        item.setPurchaseRequests(getNewPurchaseListWithoutSelectedPurchaseRequest(item, purchaseRequest));
        item.setSold(true);
        itemService.save(item);
        manService.saveMan(man);
        purchaseRequestRepository.delete(purchaseRequest);
    }

    private List<PurchaseRequest> getNewPurchaseListWithoutSelectedPurchaseRequest(Man man, PurchaseRequest purchaseRequest) {
        List<PurchaseRequest> oldPurchaseList = findByMan(man);
        oldPurchaseList.remove(purchaseRequest);
        return oldPurchaseList;
    }

    private List<PurchaseRequest> getNewPurchaseListWithoutSelectedPurchaseRequest(Item item, PurchaseRequest purchaseRequest) {
        List<PurchaseRequest> oldPurchaseList = findByItem(item);
        oldPurchaseList.remove(purchaseRequest);
        return oldPurchaseList;
    }

    public void deleteAllConnectionsFromPurchaseList(List<PurchaseRequest> purchaseRequests) {
        for (PurchaseRequest purchaseRequest : purchaseRequests) {
            delete(purchaseRequest);
        }
    }


}
