package com.example.demo.constant;

/**
 * @author : wulg29230
 * @Date : 2020/3/6 13:14
 **/
public enum BusinessExceptionEnum {
    GET_REDIS_LOCK_FAIL(1001,"获取分布式锁失败"),
    USER_NOT_EXITS(4040,"该员工不存在"),
    RABBIT_MQ_INIT_FAIL(4041,"rabbitmq连接生产者生产失败"),
    SYSTEM_ERROR(5000,"系统内部错误");

    private String code;
    private String msg;

    BusinessExceptionEnum(Integer code,String msg){
        this.code = code.toString();
        this.msg = msg;
    }
    public Integer getErrorNo(){
        return Integer.valueOf(this.code);
    }

    public String getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }
}
