package com.example.PPQ.respository;

import com.example.PPQ.Entity.Roles_Entity;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
@Repository
public interface Roles_respository extends JpaRepository<Roles_Entity, Integer> {
    public Roles_Entity findByRoleName(String rolename);

}
