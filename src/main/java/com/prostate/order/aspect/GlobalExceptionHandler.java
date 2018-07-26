/**
 * 全局异常捕获
 */
package com.prostate.order.aspect;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Map<String,Object> resultMap;
    /**
     * 所有异常报错
     *
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
   @ExceptionHandler(value = Exception.class)
    public Map<String,Object> allExceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
        resultMap = new LinkedHashMap<>();
        log.error("我报错了：{}",exception.getLocalizedMessage());
        log.error("我报错了：{}",exception.getCause());
        log.error("我报错了：{}",exception.getSuppressed());
        log.error("我报错了：{}",exception.getMessage());
        log.error("我报错了：{}",exception.getStackTrace());

        resultMap.put("errorcode","50000");
        resultMap.put("errormsg",exception.getLocalizedMessage());
        return resultMap;
    }
    //拦截  validate注解
    @ExceptionHandler(value = BindException.class)
    public Map<String,Object> bindExceptionHandler(HttpServletRequest request, BindException exception) throws Exception {
        resultMap = new LinkedHashMap<>();

        resultMap.put("code","40001");
        resultMap.put("msg","FAILED_PARAM");
        resultMap.put("result",fieldErrorsBuilder(exception.getFieldErrors()));

        return resultMap;
    }

    private static Map<String,Object> fieldErrorsBuilder(List<FieldError> fieldErrorList){
        Map<String,Object> fieldErrorsMap = new LinkedHashMap<>();
        for (FieldError fieldError : fieldErrorList) {
            fieldErrorsMap.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
        return fieldErrorsMap;
    }
}
