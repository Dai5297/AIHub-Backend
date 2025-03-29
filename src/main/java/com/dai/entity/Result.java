package com.dai.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回结果类
 * @author dai
 * @param <T>
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
    /**
     * 响应返回编码
     */
    private int code;

    /**
     * 响应返回信息
     */
    private String msg;

    /**
     * 返回结果
     */
    private T data;

    public static Result success(){
        Result result = new Result<>();
        result.code = HttpStatus.SUCCESS;
        result.msg = "操作成功";
        result.data = null;
        return result;
    }

    public static Result success(Object data){
        Result result = new Result<>();
        result.code = HttpStatus.SUCCESS;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }

    public static Result success(String msg,Object data){
        Result result = new Result<>();
        result.code = HttpStatus.SUCCESS;
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static Result error(){
        Result result = new Result<>();
        result.code = HttpStatus.ERROR;
        result.msg = "操作失败";
        result.data = null;
        return result;
    }

    public static Result error(int code) {
        Result result = new Result<>();
        result.code = code;
        result.msg = "操作失败";
        result.data = null;
        return result;
    }

    public static Result error(String msg){
        Result result = new Result<>();
        result.code = HttpStatus.ERROR;
        result.msg = msg;
        result.data = null;
        return result;
    }

    public static Result error(int code,String msg){
        Result result = new Result<>();
        result.code = code;
        result.msg = msg;
        result.data = null;
        return result;
    }

    public static Result error(int code,String msg,Object data){
        Result result = new Result<>();
        result.code = code;
        result.msg = msg;
        result.data = data;
        return result;
    }
}
