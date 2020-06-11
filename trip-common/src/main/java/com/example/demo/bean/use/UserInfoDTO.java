package com.example.demo.bean.use;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : wulg29230
 * @Date : 2020/3/6 9:43
 **/
@Data
public class UserInfoDTO implements Serializable {
    private Long userId;
    private String userName;
    private String password;
}
