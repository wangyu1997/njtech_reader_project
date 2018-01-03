package com.reader.demo.reader.Model;

import lombok.Data;

@Data
public class HttpResponse<T> {
    public final static Integer OK = 200;
    public final static Integer ERROR = 500;

    private Integer code;
    private String message;
    private T data;

    public HttpResponse() {
        code = OK;
    }

    public HttpResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
