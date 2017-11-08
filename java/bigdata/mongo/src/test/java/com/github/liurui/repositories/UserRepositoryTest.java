package com.github.liurui.repositories;

import com.github.liurui.App;
import com.github.liurui.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testCRUD() {

        Calendar date = Calendar.getInstance();
        date.set(2017, 11, 2, 10, 12, 3);

        User user = new User();

        user.setId("1");
        user.setName("张三");
        user.setCreateDate(date.getTime());
        try {
            userRepository.save(user);

            user = userRepository.findById("1").get();
            assertEquals("张三", user.getName());
            assertEquals(date.getTime(), user.getCreateDate());
        } finally {
            userRepository.deleteById("1");
        }
    }
}