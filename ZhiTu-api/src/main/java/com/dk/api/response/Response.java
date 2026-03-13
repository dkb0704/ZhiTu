package com.dk.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 7000723935764546321L;

    private String code;
    private String info;
    private T data;

    /** 成功响应（无数据） */
    public static <T> Response<T> success() {
        return Response.<T>builder().code("0000").info("成功").build();
    }

    /** 成功响应（带数据） */
    public static <T> Response<T> success(T data) {
        return Response.<T>builder().code("0000").info("成功").data(data).build();
    }

    /** 失败响应 */
    public static <T> Response<T> fail(String info) {
        return Response.<T>builder().code("0001").info(info).build();
    }
}
