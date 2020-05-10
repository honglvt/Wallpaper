package com.xieaoze.novawallpaper.base;

import com.xieaoze.novawallpaper.R;
import com.yechaoa.yutils.YUtils;

import java.io.IOException;

public class BaseException extends IOException {

    /**
     * 解析数据失败
     */
    public static final String PARSE_ERROR_MSG = YUtils.getApplication().getResources().getString(R.string.parse_error);
    /**
     * 网络问题
     */
    public static final String BAD_NETWORK_MSG = YUtils.getApplication().getResources().getString(R.string.bad_network);
    /**
     * 连接错误
     */
    public static final String CONNECT_ERROR_MSG = YUtils.getApplication().getResources().getString(R.string.connect_error);
    /**
     * 连接超时
     */
    public static final String CONNECT_TIMEOUT_MSG = YUtils.getApplication().getResources().getString(R.string.connect_timeout);
    /**
     * 未知错误
     */
    public static final String OTHER_MSG = YUtils.getApplication().getResources().getString(R.string.other_error);

    private String msg;
    private int code;

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public BaseException(String message) {
        this.msg = message;
    }

    public BaseException(String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
    }

    public BaseException(int code, String message) {
        this.msg = message;
        this.code = code;
    }

}