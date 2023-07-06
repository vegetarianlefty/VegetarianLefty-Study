package com.vegetarianlefty.common.response;

import lombok.Data;

/**
 * 返回信息
 *
 * @date 2023/2/2 15:10
 */
@Data
public class Res {
    /**
     * 服务状态
     */
    private Integer status;
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 信息
     */
    private String message;
    /**
     * 返回结果
     */
    private Object data;

    public static Res get(ServiceStatus serviceStatus, Object data) {
        Res res = new Res();
        res.setCode(serviceStatus.getCode());
        res.setStatus(serviceStatus.getStatus());
        res.setMessage(serviceStatus.getMessage());
        res.setData(data);
        return res;
    }

    public static Res get(ServiceStatus serviceStatus) {
        Res res = new Res();
        res.setCode(serviceStatus.getCode());
        res.setStatus(serviceStatus.getStatus());
        res.setMessage(serviceStatus.getMessage());
        return res;
    }

    public static Res get(ServiceStatus serviceStatus, String message) {
        Res res = new Res();
        res.setCode(serviceStatus.getCode());
        res.setStatus(serviceStatus.getStatus());
        res.setMessage(message);
        return res;
    }

    public static Res ok(Object data) {
        return get(ServiceStatus.GET.OK, data);
    }

    public static Res unauthorized() {
        return Res.get(ServiceStatus.GENERAL.UNAUTHORIZED);
    }
    public static Res created() {
        return Res.get(ServiceStatus.CREATE.CREATED);
    }
    public static Res notCreated() {
        return Res.get(ServiceStatus.CREATE.NOT_CREATED);
    }
    public static Res exists() {
        return Res.get(ServiceStatus.CREATE.EXISTS);
    }
    public static Res duplicateRequest() {
        return Res.get(ServiceStatus.GENERAL.DUPLICATE_REQUEST);
    }

    public static  Res badRequest(String message){
        return get(ServiceStatus.GENERAL.BAD_REQUEST,message);
    }
}
