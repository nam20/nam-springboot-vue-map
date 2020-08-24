package com.example.demo.Repository;

import com.example.demo.DB.Entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {

    UserRepository userRepository;

    @Test
    void findByUserName() {
        User user = User.builder()
                .userId("333")
                .userName("333")
                .userPassword("3333")
                .build();
    }
}