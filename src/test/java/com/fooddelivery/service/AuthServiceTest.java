package com.fooddelivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fooddelivery.domain.User;
import com.fooddelivery.exception.AuthenticationException;
import com.fooddelivery.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    UserRepository users;
    @InjectMocks
    AuthService auth;

    @Test
    void validCustomerLogin() {
        User c = new User("1", "Ali", "ali@x.com", "pw", "CUSTOMER");
        when(users.findByEmail("ali@x.com")).thenReturn(c);
        assertSame(c, auth.login("ali@x.com", "pw"));
    }

    @Test
    void validManagerLogin() {
        User m = new User("2", "Sara", "sara@x.com", "secret", "MANAGER");
        when(users.findByEmail("sara@x.com")).thenReturn(m);
        assertSame(m, auth.login("sara@x.com", "secret"));
    }

    @Test
    void wrongPasswordRejected() {
        when(users.findByEmail("ali@x.com"))
                .thenReturn(new User("1", "Ali", "ali@x.com", "pw", "CUSTOMER"));
        AuthenticationException ex = assertThrows(AuthenticationException.class,
                () -> auth.login("ali@x.com", "bad"));
        assertEquals(AuthService.INVALID_CREDENTIALS, ex.getMessage());
    }

    @Test
    void unknownEmailRejected() {
        when(users.findByEmail("none@x.com")).thenReturn(null);
        assertThrows(AuthenticationException.class, () -> auth.login("none@x.com", "pw"));
    }

    @Test
    void nullInputsRejected() {
        assertThrows(AuthenticationException.class, () -> auth.login(null, "pw"));
        assertThrows(AuthenticationException.class, () -> auth.login("ali@x.com", null));
    }
}
