package com.vegetarianlefty.common.response;

import lombok.Getter;

/**
 * <p>服务状态信息</p>
 *
 * @date 2023/2/2 15:17
 */
public interface ServiceStatus {

    /**
     * 获取服务状态
     *
     * @return 0/1
     */
    int getStatus();

    /**
     * 获取状态码
     *
     * @return 由实现{@link ServiceStatus}的枚举决定
     */
    int getCode();

    /**
     * 获取信息
     *
     * @return 由实现{@link ServiceStatus}的枚举决定
     */
    String getMessage();

    /**
     * <p>通用的</p>
     *
     * @author wxh_work@163.com
     * @date 2023/2/2 15:17
     */
    @Getter
    enum GENERAL implements ServiceStatus {
        /**
         * 请求参数有误
         */
        BAD_REQUEST(0, 40000, "请求参数有误"),
        /**
         * 权限错误
         */
        UNAUTHORIZED(0, 40100, "权限错误"),
        /**
         * 无效Token
         */
        INVALID_TOKEN(0, 40101, "无效Token"),
        /**
         * 重复提交
         */
        DUPLICATE_REQUEST(0, 40102, "重复提交，请稍后重试"),
        /**
         * 禁止的请求
         */
        FORBIDDEN(0, 40300, "禁止的请求"),
        /**
         * 服务错误
         */
        SERVICE_ERROR(0, 50000, "服务内部错误，请稍后重试"),
        /**
         * <pre>
         * 系统繁忙
         * 1.在有锁保护的接口中获取锁失败
         */
        SERVICE_BUSY(0, 50001, "系统繁忙，请稍后重试");
        /**
         * 服务状态
         */
        private final int status;
        /**
         * 状态码
         */
        private final int code;
        /**
         * 信息
         */
        private final String message;

        GENERAL(int status, int code, String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }
    }

    /**
     * <p>查询</p>
     *
     * @author wxh_work@163.com
     * @date 2023/2/2 15:17
     */
    @Getter
    enum GET implements ServiceStatus {
        /**
         * 查询成功
         */
        OK(1, 20000, "查询成功"),
        /**
         * 无查询数据
         */
        NOT_FOUND(0, 40400, "无查询数据");
        /**
         * 服务状态
         */
        private final int status;
        /**
         * 状态码
         */
        private final int code;
        /**
         * 信息
         */
        private final String message;

        GET(int status, int code, String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }
    }

    /**
     * <p>创建</p>
     *
     * @author wxh_work@163.com
     * @date 2023/2/2 15:17
     */
    @Getter
    enum CREATE implements ServiceStatus {
        /**
         * 创建成功
         */
        CREATED(1, 20000, "创建成功"),
        /**
         * 创建失败
         */
        NOT_CREATED(1, 20101, "创建失败"),
        /**
         * 已存在数据
         */
        EXISTS(0, 20102, "已存在数据");
        /**
         * 服务状态
         */
        private final int status;
        /**
         * 状态码
         */
        private final int code;
        /**
         * 信息
         */
        private final String message;

        CREATE(int status, int code, String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }
    }

    /**
     * <p>更新</p>
     *
     * @author wxh_work@163.com
     * @date 2023/2/2 15:17
     */
    @Getter
    enum UPDATE implements ServiceStatus {
        /**
         * 更新成功
         */
        ACCEPTED(1, 20000, "更新成功"),
        /**
         * 更新失败
         */
        NOT_ACCEPTED(0, 20201, "更新失败"),
        /**
         * 使用乐观锁保护的数据，更新时版本不一致
         **/
        VERSION_ERROR(0, 20202, "请求过期，提交失败");
        /**
         * 服务状态
         */
        private final int status;
        /**
         * 状态码
         */
        private final int code;
        /**
         * 信息
         */
        private final String message;

        UPDATE(int status, int code, String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }
    }

    /**
     * <p>更新</p>
     *
     * @author wxh_work@163.com
     * @date 2023/2/2 15:17
     */
    @Getter
    enum DEL implements ServiceStatus {
        /**
         * 删除成功
         */
        ACCEPTED(1, 20000, "删除成功"),
        /**
         * 删除失败
         */
        NOT_ACCEPTED(0, 20201, "删除失败"),
        /**
         * 无查询数据
         */
        NOT_FOUND(0, 40400, "无删除数据");
        /**
         * 服务状态
         */
        private final int status;
        /**
         * 状态码
         */
        private final int code;
        /**
         * 信息
         */
        private final String message;

        DEL(int status, int code, String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }
    }
}
