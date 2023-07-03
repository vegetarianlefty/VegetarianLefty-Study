package com.vegetarianlefty.rpc;

import com.vegetarianlefty.UserRpcService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * description
 *
 * @date 2022/12/28 00:48
 */
@DubboService(protocol = "dubbo", version = "2.0.0", retries = 3)
public class UserRpcServiceImpl implements UserRpcService {
    @Override
    public String getUserName(int userId) {
        System.out.println(userId);
        return "dubbo";
    }
}
