package com.vegetarianlefty.controller;


import com.vegetarianlefty.common.response.Res;
import com.vegetarianlefty.vo.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class UserController {

    @PostMapping("/userList")
    public Res userList(@Valid @RequestBody User user) {
        System.out.println(user);
        return Res.ok("success");
    }
}
