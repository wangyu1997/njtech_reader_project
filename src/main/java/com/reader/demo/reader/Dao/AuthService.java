package com.reader.demo.reader.Dao;

import com.reader.demo.reader.Model.SysUser;

import java.util.Map;

public interface AuthService {
    SysUser register(String headSrc,String username, String password, String email, String name) throws Exception;

    Map<String, Object> login(String username, String password) throws Exception;

    String refresh(String oldToken);

    SysUser getCurrentUser(String oldToken);
}  