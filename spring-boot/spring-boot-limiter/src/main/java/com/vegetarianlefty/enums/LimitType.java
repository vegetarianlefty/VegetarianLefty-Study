package com.vegetarianlefty.enums;

/**
 * description
 *
 * @date 2023/7/6 14:21
 */
public enum LimitType {
    /**

     * 默认策略全局限流

     */

    DEFAULT,

    /**

     * 根据请求者客户端进行限流

     */
    CUSTOMER,

    /**

     * 根据请求者IP进行限流

     */

    IP
}
