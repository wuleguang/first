package com.example.demo.bean;

import lombok.Data;

/**
 * @author : wulg29230
 * @Date : 2020/3/6 9:24
 **/
@Data
public class Result<T> {
    /**
     * 错误信息
     */
    private int errorCode;

    /**
     * 错误编码
     */
    private String errorInfo;

    /**
     * 返回的信息
     */
    private T data;

    public Result() {
    }

    public Result(int errorCode, String errorInfo) {
        this.errorCode = errorCode;
        this.errorInfo = errorInfo;
    }
}
