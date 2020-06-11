package com.example.demo.constant;

/**
 * @author : wulg29230
 * @Date : 2020/3/6 9:56
 **/
public class BusinessEnum {
    public enum Result{
        OK(0000,"OK"),EXCEPTION(9999,"系统内部错误");
        private Integer resultCode;
        private String resultInfo;
        Result(Integer resultCode,String resultInfo){
            this.resultCode = resultCode;
            this.resultInfo = resultInfo;
        }
        public Integer getResultCode(){
            return resultCode;
        }

        public String getResultInfo() {
            return  resultInfo;
        }
    }
}
