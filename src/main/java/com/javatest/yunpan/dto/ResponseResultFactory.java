package com.javatest.yunpan.dto;

public class ResponseResultFactory {

    public static ResponseResult getResponseResult(boolean success, String message) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(success);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static <T> ResponseResult getResponserResult(boolean success, T data) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setSuccess(success);
        responseResult.setData(data);
        return responseResult;
    }
}
