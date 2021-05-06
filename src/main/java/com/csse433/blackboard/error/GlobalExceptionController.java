package com.csse433.blackboard.error;

import com.csse433.blackboard.common.Result;
import com.csse433.blackboard.error.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chetzhang
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

    @Value("${application.config.show-detailed-error-message}")
    private Boolean enableDebugging;

    @ExceptionHandler(value = GeneralException.class)
    public Result<?> generalExceptionHandler(Exception e){
        log.info(e.getMessage(), e.getCause());
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result<?> missingParamHandler(Exception e) {
        log.info(e.getMessage(), e.getCause());
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Result<?> exceptionHandler(Exception e){
        log.error(e.getMessage(), e.getCause());
        e.printStackTrace();
        if(enableDebugging){
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }
        return Result.fail("Unknown Error.");
    }

}
