package com.reader.demo.reader.Controller.Public;

import com.reader.demo.reader.Dao.AuthService;
import com.reader.demo.reader.Model.HttpResponse;
import com.reader.demo.reader.Model.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Api("用户认证相关api")
public class AuthController {
    @Value("token")
    private String tokenHeader;

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "用户登陆", notes = "根据用户名密码进行登陆,返回当前用户信息和token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public HttpResponse<?> createAuthenticationToken(
            String username, String password
    ) throws Exception {
        //  @RequestBody JwtAuthenticationRequest authenticationRequest  
        Map map = authService.login(username, password);
        // Return the token  
        return new HttpResponse<>(200, "ok", map);
    }

    @ApiOperation(value = "用户token刷新", notes = "本地保存token，在下次使用时可以获得新的token，延长token时间，免登录")
    @RequestMapping(value = "/auth/refresh", method = RequestMethod.GET)
    public HttpResponse<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return new HttpResponse<>(HttpResponse.ERROR, "token refresh failed", null);
        } else {
            return new HttpResponse<>(HttpResponse.OK, "ok", refreshedToken);
        }
    }

    @ApiOperation(value = "用户注册", notes = "根据用户名密码进行注册,返回用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户昵称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "用户名字", required = true, dataType = "String")
    })
    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public SysUser register(String username, String password, String email, String name) throws Exception {
        return authService.register(username, password, email, name);
    }
}  