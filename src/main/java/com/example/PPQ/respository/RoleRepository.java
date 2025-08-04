package com.example.PPQ.respository;

import com.example.PPQ.Entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<RolesEntity, Integer> {
    public RolesEntity findByRoleName(String rolename);

    List<RolesEntity> findAllByIdIn(Set<Integer> ids);
}
