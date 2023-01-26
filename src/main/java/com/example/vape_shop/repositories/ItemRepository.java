package com.example.vape_shop.repositories;

import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllByMan(Man man);

    List<Item> findByItemNameContainingIgnoreCaseOrderByItemPrise(String name);

    List<Item> findByItemNameContainingIgnoreCaseOrderByItemPriseDesc(String name);

    List<Item> findByItemNameContainingIgnoreCaseOrderByItemDateOfCreateDesc(String name);

    @Query("select i from Item i left join PurchaseRequest pr on i.itemId = pr.item.itemId where i.isSold = false and i.itemChecked = 'YES' and lower(i.itemName) like %:name% GROUP BY(i.itemId) ORDER BY (COUNT(pr.purchaseRequestId)) DESC")
    List<Item> findByItemNameContainingIgnoreCaseOrderAndSortByAmountOfPurchaseRequest(@Param("name") String name);

    @Query(value = "select i from Item i join Man m on i.man.userId = ?1 where i.itemChecked = ?2 and i.isSold = false order by i.itemDateOfCreate")
    List<Item> findAllByManAndItemCheckedAndIsSoldEqualFalse(int idOwner, String itemChecked);

    List<Item> findAllByItemChecked(String itemChecked);

    List<Item> findAllByBuyer(Man buyer);
}
