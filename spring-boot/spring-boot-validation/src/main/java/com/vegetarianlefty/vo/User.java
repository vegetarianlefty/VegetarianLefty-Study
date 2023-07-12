package com.vegetarianlefty.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * description
 *
 * @date 2023/7/12 11:12
 */
@Data
public class User {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不为空")
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "密码不为空")
    private String password;
}
