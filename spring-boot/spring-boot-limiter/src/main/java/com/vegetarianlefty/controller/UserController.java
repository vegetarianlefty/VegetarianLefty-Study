package com.vegetarianlefty.controller;

import com.vegetarianlefty.annotation.Limit;
import com.vegetarianlefty.annotation.RedisLimit;
import com.vegetarianlefty.common.response.Res;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description
 *
 * @date 2022/10/12 15:08
 */
@RestController
public class UserController {

    @PostMapping("/userList")
    @Limit(key = "userList", permitsPerSecond = 1, timeout = 500, msg = "当前排队人数较多，请稍后再试!")
    public Res userList() {
        System.out.println("接口限流开始");
        return Res.ok("接口限流，正常访问");
    }

    @PostMapping("/userList1")
    @RedisLimit(key = "userList1", count = 2, period = 2, msg = "当前排队人数较多，请稍后再试！")
    public Res userList1() {
        System.out.println("接口限流开始");
        return Res.ok("接口限流，正常访问");
    }
}
