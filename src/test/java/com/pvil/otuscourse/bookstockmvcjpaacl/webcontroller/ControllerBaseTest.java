package com.pvil.otuscourse.bookstockmvcjpaacl.webcontroller;

import com.pvil.otuscourse.bookstockmvcjpaacl.service.UsersService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

public class ControllerBaseTest {
    @MockBean
    protected UsersService usersService;

    @MockBean
    protected UserDetailsService userDetailsService;

}
