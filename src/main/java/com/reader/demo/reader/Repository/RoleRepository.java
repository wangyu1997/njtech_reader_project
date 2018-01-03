package com.reader.demo.reader.Repository;


import com.reader.demo.reader.Model.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<SysRole, Long> {
    SysRole findSysRoleByName(String name);

    SysRole findSysRoleById(Long id);
}
