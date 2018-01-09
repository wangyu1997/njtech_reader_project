package com.reader.demo.reader.Controller.Admin;

import com.reader.demo.reader.Model.HttpResponse;
import com.reader.demo.reader.Model.SysRole;
import com.reader.demo.reader.Model.SysUser;
import com.reader.demo.reader.Repository.RoleRepository;
import com.reader.demo.reader.Repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Api("管理员用户信息管理api")
@RequestMapping("/admin/users")
public class AdminUserController {

    final UserRepository userRepository;
    final RoleRepository roleRepository;

    @Autowired
    public AdminUserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @ApiOperation(value = "管理员查询所有用户信息", notes = "查询所有用户信息", httpMethod = "GET")
    @GetMapping("")
    public HttpResponse<?> findAllUsers() {
        List<SysUser> users = userRepository.findAll();
        HttpResponse<List<SysUser>> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("success");
        httpResponse.setData(users);
        return httpResponse;
    }

    @ApiOperation(value = "管理员根据id查询用户信息", notes = "查询用户信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "Long", paramType = "path"),
    })
    @GetMapping("/{id}")
    public HttpResponse<?> findUserById(@PathVariable Long id) {
        SysUser user = userRepository.findSysUserById(id);
        HttpResponse<SysUser> response = new HttpResponse<>();
        response.setMessage("Success");
        response.setData(user);
        return response;
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "headSrc", value = "用户头像", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "email", value = "用户邮箱", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "用户名字", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "roles", value = "权限id 用分号隔开", dataType = "String", paramType = "form")
    })
    @PutMapping("/{id}")
    public HttpResponse<?> updateBook1(@PathVariable Long id, String headSrc, String password, String email, String name, String roles) throws Exception {
        SysUser user = userRepository.findSysUserById(id);
        if (user == null)
            throw new Exception("The User which id is " + id + " is not exist");
        if (headSrc!=null&&!headSrc.isEmpty())
            user.setHeadSrc(headSrc);
        if (password!=null&&!password.isEmpty()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(password));
        }
        if (email!=null&&!email.isEmpty())
            user.setEmail(email);
        if (name!=null&&!name.isEmpty())
            user.setName(name);
        if (roles!=null&&!roles.isEmpty()) {
            List<SysRole> roleList = getRolesByString(roles);
            user.setRoles(roleList);
        }
        HttpResponse<SysUser> httpResponse = new HttpResponse<>();
        if (userRepository.save(user) != null) {
            httpResponse.setMessage("success");
            httpResponse.setData(user);
        } else httpResponse.setMessage("failed");
        return httpResponse;
    }

    @ApiOperation(value = "删除用户", notes = "删除用户信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签Id", required = true, dataType = "Long", paramType = "path")
    })
    @PostMapping("/delete/{id}")
    public HttpResponse<?> deleteUser(@PathVariable Long id) throws Exception {
        HttpResponse<String> httpResponse = new HttpResponse<>();
        if (userRepository.deleteUserById(id) == 1)
            httpResponse.setMessage("success");
        else {
            httpResponse.setMessage("failed");
            httpResponse.setCode(HttpResponse.ERROR);
        }
        httpResponse.setData("");
        return httpResponse;
    }


    private List<SysRole> getRolesByString(String role_id) throws Exception {
        String[] r_ids = role_id.split(";");
        List<SysRole> roles = new ArrayList<>();
        SysRole role;
        for (String id : r_ids) {
            if (id.trim().isEmpty())
                continue;
            Long roleId = Long.valueOf(id);
            role = roleRepository.findSysRoleById(roleId);
            if (role == null)
                throw new Exception("There are no such role which id is " + id);
            roles.add(role);
        }
        return roles;
    }
}
