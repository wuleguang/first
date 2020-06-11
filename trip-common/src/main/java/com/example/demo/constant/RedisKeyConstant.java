package com.example.demo.constant;

/**
 * @author : wulg29230
 * @Date : 2020/3/9 13:37
 **/
public class RedisKeyConstant {

    /**
     * 锁前缀
     */
    public static final String Lock = "redisLock_";

    public static String getRedisKey(String prefix,String key){
        return new StringBuilder(prefix).append(key).toString();
    }
}
