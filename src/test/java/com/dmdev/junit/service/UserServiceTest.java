package com.dmdev.junit.service;

import com.dmdev.junit.dao.UserDao;
import com.dmdev.junit.dto.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

//import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "111");

    private UserDao userDao;
    private UserService userService;


    @BeforeAll
    void init() {
        System.out.println("Before All");
    }

    @BeforeEach
    void prepare() {
        System.out.println("Before: " + this);
        this.userDao = Mockito.mock(UserDao.class);
        userService = new UserService(userDao);
    }

    @Test
    void shouldDeleteExistedUser() {
        userService.addUser(IVAN);

        Mockito.doReturn(true).when(userDao).delete(Mockito.anyInt());

//        Mockito.when(userDao.delete(IVAN.getId()))
//                .thenReturn(true)
//                .thenReturn(false);

        boolean deleteResult = userService.delete(IVAN.getId());

        System.out.println(userService.delete(IVAN.getId()));
        System.out.println(userService.delete(IVAN.getId()));
        assertTrue(deleteResult);
    }

    @Test
    void loginSuccessIfUserExists() {
        userService.addUser(IVAN);

        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals(IVAN, user));
    }

    @Test
    void throwExceptionIfUsernameOrPasswordIsNull() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> userService.login(null, "1234")),
                () -> assertThrows(IllegalArgumentException.class, () -> userService.login("help", null)),
                () -> assertThrows(IllegalArgumentException.class, () -> userService.login("", "1234")),
                () -> assertThrows(IllegalArgumentException.class, () -> userService.login("help", ""))
        );
    }

    @Test
    void loginFailIfUserDoesNotExist() {
        userService.addUser(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), "dummy");
        assertTrue(maybeUser.isEmpty());
    }

    @Test
    public void usersSizeIfUserAdded() {
        userService.addUser(IVAN);
        userService.addUser(PETR);

        List<User> users = userService.getAll();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }
}