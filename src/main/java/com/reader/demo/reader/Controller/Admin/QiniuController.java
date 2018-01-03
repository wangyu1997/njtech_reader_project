package com.reader.demo.reader.Controller.Admin;

import com.google.gson.Gson;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.reader.demo.reader.Model.HttpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api("七牛认证相关api")
@RequestMapping("/admin/qiniu")
public class QiniuController {
    @ApiOperation(value = "获取七牛云token", notes = "获取七牛token")
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public HttpResponse<Map<String, String>> qiNiuToken() {
        String accessKey = "0QTSvPXnvfdzIVJfwGoK1sFkBzhfiVw9mwUw8n6A";
        String secretKey = "lyqOdY3wt7jkCHW3Czc1ltaBwLPgpuoeCTMJtGOC";
        String bucket = "reader";
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        HttpResponse<Map<String, String>> response = new HttpResponse<>();
        Map<String, String> map = new HashMap<>();
        map.put("key", "${key}");
        map.put("hash", "${hash}");
        map.put("bucket", "bucket");
        map.put("fsize", "$(fsize)");
        Gson gson = new Gson();
        response.setData(map);
        response.setMessage("success");
        String callBack = gson.toJson(response);
        putPolicy.put("returnBody", callBack);
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
        HttpResponse<Map<String, String>> tokenResponse = new HttpResponse<>();
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", upToken);
        tokenResponse.setData(tokenMap);
        tokenResponse.setMessage("success");
        return tokenResponse;
    }
}
