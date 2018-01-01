package com.reader.demo.reader.Dao.Impl;

import com.reader.demo.reader.Dao.AuthService;
import com.reader.demo.reader.Model.SysRole;
import com.reader.demo.reader.Model.SysUser;
import com.reader.demo.reader.Repository.RoleRepository;
import com.reader.demo.reader.Repository.UserRepository;
import com.reader.demo.reader.Util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private final static String USER_PROPERTY = "ROLE_USER";

    @Value("Bearer")
    private String tokenHead;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            UserRepository userRepository,
            RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    public SysUser register(String username, String password, String email, String name) throws Exception {
        if (userRepository.findUserByUsername(username) != null) {
            throw new Exception("Username is already exist");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        SysUser user = new SysUser();
        SysRole role = roleRepository.findSysRoleByName(AuthServiceImpl.USER_PROPERTY);
        if (role == null)
            throw new Exception("no user role exist");
        List<SysRole> roleList = user.getRoles();
        roleList.add(role);
        user.setRoles(roleList);
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setEmail(email);
        user.setName(name);
        user.setLastPasswordResetDate(new Date());
        return userRepository.save(user);
    }

    @Override
    public Map<String, Object> login(String username, String password) throws Exception {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security  
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Reload password post-security so we can generate token  
        SysUser user = (SysUser) userDetailsService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(user);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", token);
        return map;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        SysUser user = (SysUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}  