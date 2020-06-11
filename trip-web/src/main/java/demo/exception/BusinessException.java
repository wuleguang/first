package demo.exception;

import com.example.demo.constant.BusinessExceptionEnum;

/**
 * @author : wulg29230
 * @Date : 2020/3/6 13:09
 **/
public class BusinessException extends RuntimeException {
    private String code;
    private String msg;

    public BusinessException(BusinessExceptionEnum exceptionEnum){
        this.code = exceptionEnum.getCode();
        this.msg = exceptionEnum.getMsg();
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public Integer getErrorNo(){
        return Integer.valueOf(this.code);
    }
}
