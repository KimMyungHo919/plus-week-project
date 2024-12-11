package com.example.demo;

import com.example.demo.entity.Item;
import com.example.demo.util.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testEncode() {
        // given
        String rawPassword = "password123";

        // when
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        // then
        System.out.println("Encoded Password: " + encodedPassword); // encode 값이 잘 나와야 테스트 성공.
    }

    @Test
    void testMatches() {
        // given
        String rawPassword = "password123";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        // when
        boolean matches = PasswordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertTrue(matches); // true 가 나와야 테스트 성공.
    }

    @Test
    void testWrongMatches() {
        // given
        String rawPassword = "password123";
        String wrongPassword = "adsdfsdf123";

        String encodedPassword = PasswordEncoder.encode(wrongPassword);

        // when
        boolean matches = PasswordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertFalse(matches); // false 가 나와야 테스트 성공.
    }

    @Test
    void testItem() {
        // given
        Item item = new Item("aaaa", "bbbb", null, null);

        // when
        String status = item.getStatus();

        // then
        assertNotNull(status); // null 이 아니면 텟트 성공.
    }
}
