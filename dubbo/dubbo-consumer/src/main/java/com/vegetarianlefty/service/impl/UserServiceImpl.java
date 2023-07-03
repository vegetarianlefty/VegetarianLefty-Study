package com.vegetarianlefty.service.impl;

import com.vegetarianlefty.UserRpcService;
import com.vegetarianlefty.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @date 2022/12/28 00:52
 */
@Service
public class UserServiceImpl implements UserService {

    @DubboReference(protocol = "dubbo", version = "2.0.0", retries = 3, timeout = 60000)
    private UserRpcService userRpcService;
    @Override
    public String getUser(int userId) {
        String userName = userRpcService.getUserName(userId);
        System.out.println(userName);
        return "";
    }
}
