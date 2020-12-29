package com.frame.zxmvp.basebean;

import java.io.Serializable;

/**
 * des:封装服务器返回数据
 * Created by xsf
 * on 2016.09.9:47
 */
public class BaseRespose<T> implements Serializable {
    public String code;
    public String message;
    public String msg;
    public String status;

    public T data;
    public T res;

    public boolean success() {
        return "20000".equals(code);
    }

    @Override
    public String toString() {
        return "BaseRespose{" +
                "success='" + code + '\'' +
                ", msg='" + message + '\'' +
                ", obj=" + data +
                '}';
    }

}
