# Spring Boot 参数验证

### 为什么需要来验证参数

在平常通过 Spring 框架写代码时候，会经常写接口类，相信大家对该类的写法非常熟悉。在写接口时经常要写效验请求参数逻辑，这时候我们会常用做法是写大量的 if 与 if else 类似这样的代码来做判断，如下：

```
@RestController
public class TestController {

    @PostMapping("/user")
    public String addUserInfo(@RequestBody User user) {
        if (user.getName() == null || "".equals(user.getName()) {
            ......
        } else if(user.getSex() == null || "".equals(user.getSex())) {
            ......
        } else if(user.getUsername() == null || "".equals(user.getUsername())) {
            ......
        } else {
            ......
        }
        ......
    }

}
```

这样的代码如果按正常代码逻辑来说，是没有什么问题的，不过按优雅来说，简直糟糕透了。不仅不优雅，而且如果存在大量的验证逻辑，这会使代码看起来乱糟糟，大大降低代码可读性，那么有没有更好的方法能够简化这个过程呢？

### @Valid 注解的作用

注解 @Valid 的主要作用是用于数据效验，可以在定义的实体中的属性上，添加不同的注解来完成不同的校验规则，而在接口类中的接收数据参数中添加 @valid 注解，这时你的实体将会开启一个校验的功能。

### @Valid 的相关注解

下面是 @Valid 相关的注解，在实体类中不同的属性上添加不同的注解，就能实现不同数据的效验功能。

| 注解名称                  | 注解说明                                                     |
| ------------------------- | ------------------------------------------------------------ |
| @Null                     | 限制只能为null                                               |
| @NotNull                  | 校验入参不能为空，无法正确校检长度为0的字符串或以完全为空格的字符串 |
| @AssertFalse              | 限制必须为false                                              |
| @AssertTrue               | 限制必须为true                                               |
| @DecimalMax(value)        | 限制必须为一个不大于指定值的数字，小数存在精度               |
| @DecimalMin(value)        | 限制必须为一个不小于指定值的数字，小数存在精度               |
| @Digits(integer,fraction) | 限制必须为一个小数，且整数部分的位数不能超过integer，小数部分的位数不能超过fraction |
| @Max(value)               | 限制必须为一个不大于指定值的数字                             |
| @Min(value)               | 限制必须为一个不小于指定值的数字                             |
| @Future                   | 限制必须是一个将来的日期                                     |
| @Past                     | 限制必须是一个过去的日期                                     |
| @Pattern(regexp = "")     | 使用正则表达式验证String，接受字符序列，regexp必填，所修饰为null时认为是通过验证 |
| @Size(max,min)            | 限制字符长度必须在min到max之间                               |
| @Length(min=, max=)       | 长度是否在范围内                                             |
| @Past                     | 验证注解的元素值（日期类型）比当前时间早                     |
| @NotEmpty                 | 验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0） |
| @NotBlank                 | 验证注解的元素值不为空（不为null、去除首位空格后长度为0），不同于@NotEmpty，**@NotBlank只应用于字符串且在比较时会去除字符串的空格** |
| @Email                    | 验证注解的元素值是Email，也可以通过正则表达式和flag指定自定义的email格式 |

### 使用 @Valid 进行参数效验步骤

整个过程如下图所示，用户访问接口，然后进行参数效验，因为 @Valid 不支持平面的参数效验（直接写在参数中字段的效验）所以基于 GET 请求的参数还是按照原先方式进行效验，而 POST 则可以以实体对象为参数，可以使用 @Valid 方式进行效验。如果效验通过，则进入业务逻辑，否则抛出异常，交由全局异常处理器进行处理。

![图片](http://image.wangxiaohuan.com/blog/image/202307121109289.png)

### 实体类中添加 @Valid 相关注解

使用 @Valid 相关注解非常简单，只需要在参数的实体类中属性上面添加如 @NotBlank、@Max、@Min 等注解来对该字段进限制，如下：

User：

```
public class User {
    @NotBlank(message = "姓名不为空")
    private String username;
    @NotBlank(message = "密码不为空")
    private String password;
}
```

如果是嵌套的实体对象，则需要在最外层属性上添加 @Valid 注解：

User：

```
public class User {
    @NotBlank(message = "姓名不为空")
    private String username;
    @NotBlank(message = "密码不为空")
    private String password;
    //嵌套必须加 @Valid，否则嵌套中的验证不生效
    @Valid
    @NotNull(message = "用户信息不能为空")
    private UserInfo userInfo;
}
```

UserInfo：

```
public class User {
    @NotBlank(message = "年龄不为空")
    @Max(value = 18, message = "不能超过18岁")
    private String age;
    @NotBlank(message = "性别不能为空")
    private String gender;
}
```

### 接口类中添加 @Valid 注解

在 Controller 类中添加接口，POST 方法中接收设置了 @Valid 相关注解的实体对象，然后在参数中添加 @Valid 注解来开启效验功能，需要注意的是， @Valid 对 Get 请求中接收的平面参数请求无效，稍微略显遗憾。

```
@RestController
public class Controller {

    @PostMapping("/user")
    public String addUserInfo(@Valid @RequestBody User user) {
        return "调用成功!";
    }

}
```

### 全局异常处理类中处理 @Valid 抛出的异常

最后，我们写一个全局异常处理类，然后对接口中抛出的异常进行处理，而 @Valid 配合 Spring 会抛出 MethodArgumentNotValidException 异常，这里我们需要对该异常进行处理即可。

```
@Slf4j
@Order(-1)
public abstract class AbstractGlobalExceptionHandler {


    @ExceptionHandler(value = AbstractCustomizedRunTimeException.class)
    @ResponseBody
    public Res handleCustomRunTimeException(AbstractCustomizedRunTimeException e) {
        log.error("全局异常："+e.getMessage(), e);
        return Res.get(ServiceStatus.GENERAL.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Res handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
            log.error("全局请求参数异常："+e.getMessage(), e);
            BindingResult bindingResult = e.getBindingResult();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            StringBuilder sb = new StringBuilder();
            for (ObjectError error : allErrors) {
                FieldError fieldError = (FieldError) error;
                sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(";");
            }
            return Res.get(ServiceStatus.GENERAL.BAD_REQUEST, sb.toString());
        }
}
```