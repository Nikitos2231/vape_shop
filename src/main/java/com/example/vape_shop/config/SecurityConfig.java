package com.example.vape_shop.config;

import com.example.vape_shop.services.ManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ManService manService;

    @Autowired
    public SecurityConfig(ManService manService) {
        this.manService = manService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/admin/{id}").hasRole("ADMIN")
                .antMatchers("/auth/login","/auth/registration","/main", "/main/search", "/main/sort", "/activate/*", "/items/{item_id}", "/man/{man_id}", "/error").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN", "GUEST")
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login").defaultSuccessUrl("/main", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login");
    }

    //Настраивает аутентификацию
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(manService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/css1/css/**", "/css1/**", "/css1/fonts/**",
                        "/css1/img/**", "/css1/js/**", "/css1/font/**", "/css1/font/slick.woff",
                        "/css1/ImagesForItems/**", "/css1/ImagesForPeople/**");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
