package com.reader.demo.reader.Repository;


import com.reader.demo.reader.Model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<SysUser, Long> {
    SysUser findUserByUsername(String username);

    SysUser findSysUserById(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete  from SysUser b where b.id = ?1")
    int deleteUserById(Long id);

}
