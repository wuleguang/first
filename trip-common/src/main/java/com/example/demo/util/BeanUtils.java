package com.example.demo.util;

/**
 * @author : wulg29230
 * @Date : 2020/3/6 12:24
 **/
public class BeanUtils<Dto,Do> {

    public static <Do> Do dtoTodo(Object dtoEntity,Class<Do> doClass){
        if(dtoEntity == null){
            return null;
        }
        if(doClass == null){
            return null;
        }
        try {
            Do newInstance = doClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(dtoEntity,newInstance);
            return newInstance;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <Dto> Dto doTodto(Object doEntity,Class<Dto> doClass){
        if(doEntity == null){
            return null;
        }
        if(doClass == null){
            return null;
        }
        try {
            Dto newInstance = doClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(doEntity,newInstance);
            return newInstance;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
