package com.example.vape_shop.repositories;

import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.models.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Integer> {

    List<PurchaseRequest> findByMan(Man man);

    List<PurchaseRequest> findByItem(Item item);
}
