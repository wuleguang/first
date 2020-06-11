package com.example.demo.util;

import com.example.demo.constant.RedisKeyConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : wulg29230
 * @Date : 2020/3/9 13:20
 **/
@Component
@Slf4j
public class RedisLockUtils {
    //锁超时时间1分钟
    private static final long LOCK_TIME_OUT = 60000L;

    //加锁阻塞等待时间
    private static final long THREAD_SLEEP_TIME = 500L;

    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 本地线程池
     */
    private static final ThreadLocal<Map<String,Boolean>> doubleLock = new ThreadLocal<Map<String,Boolean>>(){
        @Override
        protected Map<String,Boolean> initialValue(){
            log.info("初始化成功");
            return new HashMap<>();
        }
    };

    public Boolean lock(String key,Long timeOut){
        String lockKey = RedisKeyConstant.getRedisKey(RedisKeyConstant.Lock,key);
        log.info("获取redis锁开始，lockKey = {}",lockKey);
        try{
            while (timeOut >= 0){
                String expires = String.valueOf(System.currentTimeMillis() + LOCK_TIME_OUT);
                //如果键不存在则新增,存在则不改变已经有的值。
                boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(lockKey,expires);
                if(isSuccess){
                    doubleLock.get().put(lockKey,true);
                    log.info("获取redis锁成功，lockKey = {}",lockKey);
                    return true;
                }else{
                    //获取key键对应的值
                    Object expiresObj = redisTemplate.opsForValue().get(lockKey);
                    if(expiresObj != null) {
                        String expiresObjStr = (String)expiresObj;
                        if(Long.valueOf(expiresObjStr) < System.currentTimeMillis() ){
                            //获取原来key键对应的值并重新赋新值
                            Object expiresOldObj = redisTemplate.opsForValue().getAndSet(lockKey,expires);
                            String expiresOldObjStr = (String)expiresOldObj;
                            if(expiresOldObj != null && expiresObjStr.equals(expiresOldObjStr)){
                                doubleLock.get().put(lockKey,true);
                                log.info("获取redis锁成功，lockKey = {}",lockKey);
                                return true;
                            }
                        }
                    }
                }
                timeOut -= THREAD_SLEEP_TIME;
                Thread.sleep(THREAD_SLEEP_TIME);
            }
        }catch (Exception e){
            log.info("获取redis锁失败，lockKey = {}，exception={}",lockKey,e);
        }finally {
            log.info("获取redis锁结束，lockKey = {}",lockKey);
        }
        log.info("获取redis锁失败，lockKey = {}",lockKey);
        return false;
    }

    public void releaseLock(String key){
        String lockKey = RedisKeyConstant.getRedisKey(RedisKeyConstant.Lock,key);
        log.info("删除redis锁开始，lockKey = {}",lockKey);
        try {
            boolean isSuccess = redisTemplate.delete(lockKey);
            if(isSuccess){
                log.info("删除redis锁成功，lockKey = {}",lockKey);
                return;
            }
            log.info("没有需要删除的redis锁，lockKey = {}",lockKey);
        }catch (Exception e){
            log.info("删除redis锁失败，lockKey = {}，exception = {}",lockKey,e);
        }finally {
            doubleLock.get().remove(lockKey);
            log.info("删除redis锁结束，lockKey = {}",lockKey);
        }
    }
}
