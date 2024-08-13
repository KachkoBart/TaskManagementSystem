package org.example.taskmanagementsystem.RepositoryTest;

import org.example.taskmanagementsystem.Api.Repository.Entity.Role;
import org.example.taskmanagementsystem.Api.Repository.Entity.User;
import org.example.taskmanagementsystem.Api.Repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void finsByEmailTest() {
        User user = new User();
        user.setEmail("aaa@mail.ru");
        user.setPassword("pass");
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("aaa@mail.ru");

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getEmail()).isEqualTo("aaa@mail.ru");
        assertThat(result.get().getPassword()).isEqualTo("pass");
    }

    @Test
    public void emailNotFoundTest() {
        Optional<User> result = userRepository.findByEmail("NOUsername@mail.ru");
        assertThat(result.isPresent()).isFalse();
    }
    @Test
    public void deleteTest(){
        User user = new User();
        user.setEmail("aaa@mail.ru");
        user.setPassword("pass");
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("aaa@mail.ru");

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getEmail()).isEqualTo("aaa@mail.ru");
        assertThat(result.get().getPassword()).isEqualTo("pass");

        userRepository.delete(user);

        result = userRepository.findByEmail("aaa@mail.ru");
        assertThat(result.isPresent()).isFalse();
    }
}