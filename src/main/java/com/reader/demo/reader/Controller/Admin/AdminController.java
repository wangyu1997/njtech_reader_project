package com.reader.demo.reader.Controller.Admin;

import com.reader.demo.reader.Dao.AuthService;
import com.reader.demo.reader.Model.HttpResponse;
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

@RestController
@Api("管理员信息管理api")
@RequestMapping("/admin/info")
public class AdminController {

    @Value("token")
    private String tokenHeader;
    final AuthService authService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserRepository userRepository;

    @Autowired
    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "管理员修改密码", notes = "管理员密码修改", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "新密码", required = true, dataType = "String", paramType = "form")
    })
    @PutMapping("/password")
    public HttpResponse<?> updatePassword(String password, HttpServletRequest httpServletRequest) {
        HttpResponse<SysUser> response = new HttpResponse<>();
        String token = httpServletRequest.getHeader(tokenHeader);
        SysUser user = authService.getCurrentUser(token);
        if (user == null) {
            response.setCode(HttpResponse.ERROR);
            response.setMessage("Login First!");
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (password.isEmpty()) {
                response.setCode(HttpResponse.ERROR);
                response.setMessage("Password could not be Empty!");
            } else {
                user.setPassword(encoder.encode(password));
                if (userRepository.save(user) != null)
                    response.setMessage("success");
                else
                    response.setMessage("failed");
                response.setData(user);
            }
        }
        return response;
    }

}
