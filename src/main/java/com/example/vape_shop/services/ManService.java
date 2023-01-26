package com.example.vape_shop.services;

import com.example.vape_shop.man_util.ManChecker;
import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.repositories.ManRepository;
import com.example.vape_shop.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ManService implements UserDetailsService {
    private final ManRepository manRepository;
    private final ManChecker manChecker;

    @Autowired
    public ManService(ManRepository manRepository, ManChecker manChecker) {
        this.manRepository = manRepository;
        this.manChecker = manChecker;
    }

    public Man findOne(int id) {
        return manRepository.findById(id).orElse(null);
    }
    public List<Man> findAll() {
        return manRepository.findAll();
    }
    public Man findManByItem(Item item) {
        return manRepository.findByItems(item);
    }
    @Transactional
    public void saveMan(Man man) {
        manRepository.save(man);
    }
    public void inheritAttributesForUpdatedManFromOldMan(Man updatedMan, int oldManId) {
        Man oldMan = manRepository.findById(oldManId).orElse(null);
        updatedMan.setUserId(oldMan.getUserId());
        updatedMan.setAvatar(oldMan.getAvatar());
        updatedMan.setUserPassword(oldMan.getUserPassword());
        updatedMan.setActivationCode(oldMan.getActivationCode());
        updatedMan.setUserRole(oldMan.getUserRole());
        updatedMan.setUserEmail(oldMan.getUserEmail());
        updatedMan.setUserCountStars(oldMan.getUserCountStars());
    }

    public boolean isManUpdated(Man manForUpdating, MultipartFile imageForMan) throws IOException {
        double randomValueForDifferentNamingImages = Math.random() * 100;
        if (!manChecker.isManPrepareForUpdating(manForUpdating, imageForMan, randomValueForDifferentNamingImages)) {
            return false;
        }
        saveMan(manForUpdating);
        return true;
    }

    public Man getManByEmail(String email) {
        return manRepository.findByUserEmail(email).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Man> person = manRepository.findByUserEmail(email);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(person.get());
    }

    public List<Man> getFoundPeople(String searchMan) {
        List<Man> foundPeople = new ArrayList<>();
        if (searchMan.isEmpty()) {
            return foundPeople;
        }
        if (Pattern.matches("\\d+", searchMan)) {
            manRepository.findById(Integer.parseInt(searchMan)).ifPresent(foundPeople::add);
        }
        List<Man> foundPeopleByEmail = manRepository.findManByUserEmailContainingIgnoreCase(searchMan);
        if (!foundPeopleByEmail.isEmpty()) {
            foundPeople.addAll(foundPeopleByEmail);
        }
        return foundPeople;
    }

    public Man getEnteredMan() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails.getPerson();
    }

    public boolean isManAuth(Authentication authentication) {
        return !authentication.getPrincipal().equals("anonymousUser");
    }

}
