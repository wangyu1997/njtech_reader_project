package com.reader.demo.reader.Repository;


import com.reader.demo.reader.Model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SysUser, Long> {
    SysUser findUserByUsername(String username);
}
