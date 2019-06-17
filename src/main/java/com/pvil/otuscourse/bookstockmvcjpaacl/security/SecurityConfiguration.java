package com.pvil.otuscourse.bookstockmvcjpaacl.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/*.css");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/register").permitAll()
                    .antMatchers("/admin/*").hasAuthority(BookStockPredefinedAuthorities.ROLE_ADMIN)
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/login")
                    .and()
                .rememberMe()
                    .key("d335e240-8896-44e9-9a4e-c6f0223c8838");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    public UserDetailsService userDetailsService() {
        //Здесь возвращаем UserDetailsServiceImpl, который приходит через @Autowired. Просто наличия бина
        //реализации UserDetailsService оказалось недостаточно: это работало при form-авторизации, но
        //не находило имплементации UserDetailsService для авторизации через remember-me
        //В это ДЗ перекочивал сервис и репозитарий из монги, а вообще то есть JDBC-реализация из коробки
        return userDetailsService;
    }

}
