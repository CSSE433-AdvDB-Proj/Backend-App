package com.csse433.blackboard.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Unified Response result class.
 *
 * @author zhangx8
 */
@Data
public class Result implements Serializable {

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
    private Object data;

    //***************** Public Operations *****************

    public static Result fail() {
        return makeErrorResult(null, ERROR);
    }

    public static Result fail(Object o) {
        return makeErrorResult(o, ERROR);
    }

    public static Result fail(String message) {
        return makeErrorResult(null, message);
    }

    public static Result fail(Object o, String message) {
        return makeErrorResult(o, message);
    }

    public static Result success() {
        return makeSuccessResult(null, OK);
    }

    public static Result success(Object o) {
        return makeSuccessResult(o, OK);
    }

    public static Result success(String message) {
        return makeSuccessResult(null, message);
    }

    public static Result success(Object o, String message) {
        return makeSuccessResult(o, message);
    }

    //***************** Private Operations *****************

    private static Result makeErrorResult(Object t, String message) {
        return makeResult(t, message, ERROR_CODE);
    }

    private static Result makeSuccessResult(Object t, String message) {
        return makeResult(t, message, SUCCESS_CODE);
    }

    private static Result makeResult(Object t, String message, String code) {
        Result result = new Result();
        result.setCode(code);
        result.setData(t);
        result.setMsg(message);
        return result;
    }


}