package com.example.demo.aop;

import com.example.demo.annotation.RedisLock;
import com.example.demo.constant.BusinessExceptionEnum;
import com.example.demo.exception.BusinessException;
import com.example.demo.util.RedisLockUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * @author : wulg29230
 * @Date : 2020/3/9 14:41
 **/
@Component
@Aspect
public class RedisLockInterceptor {

    @Resource
    private RedisLockUtils redisLockUtils;

    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint pjp, RedisLock redisLock) throws Throwable {
        //切点作用的方法的参数
        Object[] args = pjp.getArgs();
        String lockKey = null;
        boolean flag = false;
        for(Object object : args) {
            //判断入参中是否有需要锁的对象
            if(object.getClass().equals(redisLock.type())) {
                Field[] fields = object.getClass().getDeclaredFields();
                for(Field field : fields){
                    field.setAccessible(true);
                    if(redisLock.key().equals(field.getName())){
                        lockKey = field.get(object) == null ? "" : field.get(object).toString();
                        flag = true;
                        break;
                    }
                }
            }
            if(flag){
                break;
            }
        }
        if(!StringUtils.isBlank(lockKey)){
            //获取redis锁
            if(redisLockUtils.lock(redisLock.key(),redisLock.waitTime())){
                try {
                    //执行方法
                    Object object = pjp.proceed();
                    return object;
                }finally {
                    //释放锁
                    redisLockUtils.releaseLock(redisLock.key());
                }
            }else{
                throw new BusinessException(BusinessExceptionEnum.GET_REDIS_LOCK_FAIL);
            }
        }else{
            //执行方法
            Object object = pjp.proceed();
            return object;
        }
    }
}
