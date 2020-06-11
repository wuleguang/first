package com.example.demo.serivce.UserService.impl;

import com.example.demo.annotation.RedisLock;
import com.example.demo.constant.BusinessExceptionEnum;
import com.example.demo.domain.UserInfo;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Resource;
import com.example.demo.bean.use.UserInfoDTO;
import com.example.demo.serivce.UserService.IUserService;
import java.util.Date;

/**
 * @author : wulg29230
 * @Date : 2020/3/6 9:46
 **/
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    //@Cacheable(value = "userInfo",key = "#userInfoDTO.userId")
    @Override
    public UserInfoDTO getUserInfo(UserInfoDTO userInfoDTO) {
        rabbitTemplate.convertAndSend("exchange.light","routingKey.light",new Message("hello,world".getBytes(),new MessageProperties()));
        UserInfo userInfo = userInfoMapper.selectByUserNameAndPassword(userInfoDTO.getUserName(),userInfoDTO.getPassword());
        if(userInfo != null){
            UserInfoDTO dto = BeanUtils.doTodto(userInfo,UserInfoDTO.class);
            return dto;
        }else{
            throw new BusinessException(BusinessExceptionEnum.USER_NOT_EXITS);
        }
    }

    //@CachePut(value = "userInfo",key = "#userInfoDTO.userId")
    @Override
    @RedisLock(type = UserInfoDTO.class,key = "userId",waitTime = 6000)
    public UserInfoDTO updateUserInfo(UserInfoDTO userInfoDTO){
        UserInfo userInfo = BeanUtils.dtoTodo(userInfoDTO,UserInfo.class);
        userInfoMapper.updateByPrimaryKey(userInfo);
        try {
            log.info("开始睡觉，time = {}",new Date());
            Thread.sleep(10000L);
            log.info("结束睡觉，time = {}",new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return BeanUtils.doTodto(userInfo,UserInfoDTO.class);
    }
}
