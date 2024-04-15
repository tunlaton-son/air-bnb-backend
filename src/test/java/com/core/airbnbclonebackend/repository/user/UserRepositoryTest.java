package com.core.airbnbclonebackend.repository.user;

import com.core.airbnbclonebackend.entity.User;
import com.core.airbnbclonebackend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("john@gmail.com", "john doe", "0822133875",  "12345678");
    }

    @Test
    public void should_save_and_fetch_user_success() {
        userRepository.save(user);
        Optional<User> userOptional = userRepository.findByEmail("john@gmail.com");
        Assertions.assertEquals(userOptional.get(), user);
        Optional<User> userOptional2 = userRepository.findByPhone("0822133875");
        Assertions.assertEquals(userOptional2.get(), user);
    }
}
