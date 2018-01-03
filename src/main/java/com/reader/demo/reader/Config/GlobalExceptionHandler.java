package com.reader.demo.reader.Config;

import com.reader.demo.reader.Model.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public HttpResponse<String> processException(HttpServletRequest req, Exception e) {
        return GlobalExceptionHandler.getResponse(e);
    }


    public static HttpResponse<String> getResponse(Exception e) {
        HttpResponse<String> r = new HttpResponse<>();
        r.setMessage(e.getMessage());
        r.setCode(HttpResponse.ERROR);
        r.setData("");
        return r;
    }

}
