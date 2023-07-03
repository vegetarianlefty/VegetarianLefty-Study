package com.vegetarianlefty;

import com.vegetarianlefty.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * description
 *
 * @date 2022/12/28 00:56
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UsetTest {
    @Resource
    private UserService userService;

    @Test
    public void userNameTest() {
        userService.getUser(123);
    }
}
