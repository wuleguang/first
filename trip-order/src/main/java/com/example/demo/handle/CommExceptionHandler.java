package com.example.demo.handle;

import com.example.demo.bean.Result;
import com.example.demo.constant.BusinessEnum;
import com.example.demo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : wulg29230
 * @Date : 2020/3/6 16:02
 **/
@Slf4j
@ControllerAdvice
public class CommExceptionHandler {

    @ResponseBody
    @ExceptionHandler
    public Result handle(Exception e){

        if(e instanceof BusinessException){
            return new Result(((BusinessException) e).getErrorNo(),((BusinessException) e).getMsg());
        }else {
            log.info("未知的异常发生",e);
            return new Result(BusinessEnum.Result.EXCEPTION.getResultCode(),BusinessEnum.Result.EXCEPTION.getResultInfo());
        }
    }
}
