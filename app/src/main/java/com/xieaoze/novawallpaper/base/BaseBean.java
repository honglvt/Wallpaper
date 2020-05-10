package com.xieaoze.novawallpaper.base;

import java.io.Serializable;

public class BaseBean<T> implements Serializable {

    public BaseBean(int code, String data) {
        this.code = code;
        this.data = (T) data;
    }

    /**
     * data :
     * errorCode : 0
     * errorMsg :
     */

    public int code;
    public String msg;
    public T data;


}