package com.example.vape_shop.repositories;

import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManRepository extends JpaRepository<Man, Integer> {

    Optional<Man> findByUserEmail(String email);

    List<Man> findManByUserEmailContainingIgnoreCase(String email);


    Man findByActivationCode(String code);

    Man findByItems(Item item);

}
