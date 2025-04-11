package com.dai.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToolResult<T> implements Serializable {

    public boolean success;

    public String msg;

    public T data;

    public static ToolResult success(Object data){
        ToolResult result = new ToolResult();
        result.success = true;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }

    public static ToolResult error(String msg){
        ToolResult result = new ToolResult();
        result.success = false;
        result.msg = msg;
        result.data = null;
        return result;
    }
}
