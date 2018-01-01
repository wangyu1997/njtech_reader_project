package com.reader.demo.reader.Repository.Implement;

import com.reader.demo.reader.Model.SysUser;
import com.reader.demo.reader.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = repository.findUserByUsername(username);
        if (sysUser == null)
            throw new UsernameNotFoundException("该用户不存在");
        else
            return sysUser;
    }
}
