package com.example.demo.serivce.UserService;


import com.example.demo.bean.use.UserInfoDTO;

/**
 * @author : wulg29230
 * @Date : 2020/3/6 9:45
 **/
public interface IUserService {
    /**
     * 获取员工信息
     * @param userInfoDTO
     * @return
     */
    UserInfoDTO getUserInfo(UserInfoDTO userInfoDTO);

    /**
     * 更新员工信息
     * @param userInfoDTO
     */
    UserInfoDTO updateUserInfo(UserInfoDTO userInfoDTO);
}
