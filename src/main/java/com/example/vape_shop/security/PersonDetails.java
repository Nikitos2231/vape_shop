package com.example.vape_shop.security;

import com.example.vape_shop.models.Man;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails {

    private Man man;

    @Autowired
    public PersonDetails(Man man) {
        this.man = man;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(man.getUserRole()));
    }

    @Override
    public String getPassword() {
        return this.man.getUserPassword();
    }

    @Override
    public String getUsername() {
        return this.man.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Нужно чтобы получать данные аутентифицированного пользователя
    public Man getPerson() {
        return this.man;
    }

}