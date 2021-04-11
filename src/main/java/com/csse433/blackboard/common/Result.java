package com.csse433.blackboard.common;


import lombok.Getter;

import java.io.Serializable;

/**
 * Unified Response result class.
 *
 * @author chetzhang
 */
@Getter
public class Result<T>  {


    /**
     * Success status code
     */
    private transient static final String SUCCESS_CODE = "0";

    /**
     * Error status code
     */
    private transient static final String ERROR_CODE = "-1";

    /**
     * Error default message
     */
    private transient static final String ERROR = "error";

    /**
     * Success default message
     */
    private transient static final String OK = "ok";

    /**
     * Status Code
     */
    private String code;

    /**
     * Message
     */
    private String msg;

    /**
     * Data
     */
    private T data;

    //***************** Public Operations *****************

    public static <T> Result<T> fail() {
        return makeErrorResult(null, ERROR);
    }

    public static <T> Result<T> fail(T t) {
        return makeErrorResult(t, ERROR);
    }

    public static <T> Result<T> fail(String message) {
        return makeErrorResult(null, message);
    }

    public static <T> Result<T> fail(T t, String message) {
        return makeErrorResult(t, message);
    }

    public static <T> Result<T> success() {
        return makeSuccessResult(null, OK);
    }

    public static <T> Result<T> success(T t) {
        return makeSuccessResult(t, OK);
    }

    public static <T> Result<T> success(String message) {
        return makeSuccessResult(null, message);
    }

    public static <T> Result<T> success(T t, String message) {
        return makeSuccessResult(t, message);
    }

    //***************** Private Operations *****************

    private static <T> Result<T> makeErrorResult(T t, String message) {
        return makeResult(t, message, ERROR_CODE);
    }

    private static <T> Result<T> makeSuccessResult(T t, String message) {
        return makeResult(t, message, SUCCESS_CODE);
    }

    private static <T> Result<T> makeResult(T t, String message, String code) {
        Result<T> result = new Result<>();
        result.code = code;
        result.data = t;
        result.msg = message;
        return result;
    }

}