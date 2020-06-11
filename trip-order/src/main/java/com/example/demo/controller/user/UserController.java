package com.example.demo.controller.user;

import com.alibaba.fastjson.JSON;
import com.example.demo.annotation.RedisLock;
import com.example.demo.bean.Result;
import com.example.demo.bean.use.UserInfoDTO;
import com.example.demo.constant.BusinessEnum;
import com.example.demo.serivce.UserService.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author : wulg29230
 * @Date : 2020/3/6 9:20
 **/
@Api(value = "用户信息相关接口")
@RestController
@RequestMapping(value = "user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(getClass().getName());
    @Resource
    private IUserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping(value = "login")
    public Result<UserInfoDTO> userLogin(@RequestBody UserInfoDTO userInfoDTO){
        Result<UserInfoDTO> result = new Result<>();
        UserInfoDTO user = userService.getUserInfo(userInfoDTO);
        redisTemplate.opsForValue().set("myUserInfo", JSON.toJSONString(userInfoDTO),20, TimeUnit.SECONDS);
        result.setData(user);
        result.setErrorCode(BusinessEnum.Result.OK.getResultCode());
        result.setErrorInfo(BusinessEnum.Result.OK.getResultInfo());
        return result;
    }

    @PostMapping(value = "update" )
    @ApiOperation(value = "更新员工信息",httpMethod = "POST")
    public Result<Object> updateUserInfo(@RequestBody UserInfoDTO userInfoDTO){
        Object myUserInfo = redisTemplate.opsForValue().get("myUserInfo");
        if(myUserInfo != null){
            logger.info("myUserInfo={}",myUserInfo);
        }
        userService.updateUserInfo(userInfoDTO);
        redisTemplate.delete("myUserInfo");
        return new Result(BusinessEnum.Result.OK.getResultCode(),BusinessEnum.Result.OK.getResultInfo());
    }

}
