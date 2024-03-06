package com.ruihang.common;


//自定义业务异常（删除关联问题）
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
