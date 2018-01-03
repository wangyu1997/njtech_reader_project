package com.reader.demo.reader.Controller.User;

import com.reader.demo.reader.Dao.AuthService;
import com.reader.demo.reader.Model.HttpResponse;
import com.reader.demo.reader.Model.SysRole;
import com.reader.demo.reader.Model.SysUser;
import com.reader.demo.reader.Repository.UserRepository;
import com.reader.demo.reader.Util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api("用户个人信息管理api")
@RequestMapping("/user/info")
public class UserInfoController {
    final UserRepository userRepository;
    @Value("token")
    private String tokenHeader;
    final AuthService authService;
    final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserInfoController(AuthService authService, JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.authService = authService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "修改个人信息", notes = "用户修改个人信息", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "headSrc", value = "用户头像", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "username", value = "用户昵称", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "用户密码",  dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "email", value = "用户邮箱",dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "用户名字",  dataType = "String", paramType = "form"),
    })
    @PutMapping("/{id}")
    public HttpResponse<?> updateBook(String headSrc, String password, String email, String name, HttpServletRequest httpServletRequest) throws Exception {
        HttpResponse<SysUser> response = new HttpResponse<>();
        String token = httpServletRequest.getHeader(tokenHeader);
        SysUser user = authService.getCurrentUser(token);
        System.out.println("\n\n\n"+user);
        if (user == null) {
            response.setCode(HttpResponse.ERROR);
            response.setMessage("Login First!");
        } else {
            if (!headSrc.isEmpty())
                user.setHeadSrc(headSrc);
            if (!password.isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(encoder.encode(password));
            }
            if (!email.isEmpty())
                user.setEmail(email);
            if (!name.isEmpty())
                user.setName(name);
            System.out.println(user);
            if (userRepository.save(user) != null) {
                response.setMessage("success");
                response.setData(user);
            } else response.setMessage("failed");
        }
        return response;
    }
}
