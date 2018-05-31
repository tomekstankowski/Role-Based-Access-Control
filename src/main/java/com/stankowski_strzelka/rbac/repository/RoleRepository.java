package com.stankowski_strzelka.rbac.repository;


import com.stankowski_strzelka.rbac.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}
